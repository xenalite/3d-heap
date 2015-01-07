using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.IO;
using System.Windows;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Microsoft.Kinect;
using System.Diagnostics;
using Microsoft.Kinect.Wpf.Controls;
using Microsoft.Kinect.Input;

namespace KinectHeap3D
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window, INotifyPropertyChanged
    {

        /// <summary>
        /// Radius of drawn hand circles
        /// </summary>
        private const double HandSize = 30;

        /// <summary>
        /// Brush used for drawing hands that are currently tracked as closed
        /// </summary>
        private readonly Brush handClosedBrush = new SolidColorBrush(Color.FromArgb(128, 255, 0, 0));

        /// <summary>
        /// Brush used for drawing hands that are currently tracked as opened
        /// </summary>
        private readonly Brush handOpenBrush = new SolidColorBrush(Color.FromArgb(128, 0, 255, 0));

        /// <summary>
        /// Brush used for drawing hands that are currently tracked as in lasso (pointer) position
        /// </summary>
        private readonly Brush handLassoBrush = new SolidColorBrush(Color.FromArgb(128, 0, 0, 255));

        /// <summary>
        /// Brush used for drawing hands that are currently tracked as in lasso (pointer) position
        /// </summary>
        private readonly Brush handUnknownBrush = new SolidColorBrush(Color.FromArgb(255, 255, 255, 255));

        /// <summary>
        /// Drawing group for body rendering output
        /// </summary>
        private DrawingGroup drawingGroup;

        /// <summary>
        /// Drawing image that we will display
        /// </summary>
        private DrawingImage imageSource;

        /// <summary>
        /// Active Kinect sensor
        /// </summary>
        private KinectSensor kinectSensor = null;

        /// <summary>
        /// Coordinate mapper to map one type of point to another
        /// </summary>
        private CoordinateMapper coordinateMapper = null;

        /// <summary>
        /// Reader for body frames
        /// </summary>
        private BodyFrameReader bodyFrameReader = null;

        /// <summary>
        /// Array for the bodies
        /// </summary>
        private Body[] bodies = null;

        /// <summary>
        /// Current status text to display
        /// </summary>
        private string statusText = null;

        /// <summary>
        /// Width of display (depth space)
        /// </summary>
        private int displayWidth;

        /// <summary>
        /// Height of display (depth space)
        /// </summary>
        private int displayHeight;

        /// <summary>
        /// Keeps track of last time, so we know when we get a new set of pointers. Pointer events fire multiple times per timestamp, based on how
        /// many pointers are present.
        /// </summary>
        private TimeSpan lastTime;


        private SocketCommunication javaCom;
        private Speech speech;

        public MainWindow()
        {

            // one sensor is currently supported
            this.kinectSensor = KinectSensor.GetDefault();

            // get the coordinate mapper
            this.coordinateMapper = this.kinectSensor.CoordinateMapper;

            // get the depth (display) extents
            FrameDescription frameDescription = this.kinectSensor.DepthFrameSource.FrameDescription;

            // get size of joint space
            this.displayWidth = frameDescription.Width;
            this.displayHeight = frameDescription.Height;

            // open the reader for the body frames
            this.bodyFrameReader = this.kinectSensor.BodyFrameSource.OpenReader();

            // set IsAvailableChanged event notifier
            this.kinectSensor.IsAvailableChanged += this.Sensor_IsAvailableChanged;

            // open the sensor
            this.kinectSensor.Open();

            // set the status text
            this.StatusText = this.kinectSensor.IsAvailable ? Properties.Resources.RunningStatusText
                                                            : Properties.Resources.NoSensorStatusText;

            // Create the drawing group we'll use for drawing
            this.drawingGroup = new DrawingGroup();

            // Create an image source that we can use in our image control
            this.imageSource = new DrawingImage(this.drawingGroup);

            // use the window object as the view model in this simple example
            this.DataContext = this;

            javaCom = new SocketCommunication("127.0.0.1", 4343);

            if (!javaCom.isConnected())
            {
                this.StatusText += " and " + Properties.Resources.SocketConnectionError;
            }

            this.speech = new Speech(kinectSensor);

            InitializeComponent();

            KinectRegion.SetKinectRegion(this, kinectRegion);

            App app = ((App)Application.Current);
            app.KinectRegion = kinectRegion;

            // Use the default sensor
            this.kinectRegion.KinectSensor = this.kinectSensor;

        }

        /// <summary>
        /// Handles kinect pointer events
        /// </summary>
        /// <param name="sender">the KinectCoreWindow</param>
        /// <param name="args">Kinect pointer args</param>
        private void kinectCoreWindow_PointerMoved(object sender, KinectPointerEventArgs args)
        {
            KinectPointerPoint kinectPointerPoint = args.CurrentPoint;
            if (lastTime == TimeSpan.Zero || lastTime != kinectPointerPoint.Properties.BodyTimeCounter)
            {
                lastTime = kinectPointerPoint.Properties.BodyTimeCounter;
            }

            if (javaCom.isConnected())
            {
                string data =
                     "engaged:" + kinectPointerPoint.Properties.IsEngaged +
                     ":x:" + kinectPointerPoint.Properties.UnclampedPosition.X +
                     ":y:" + kinectPointerPoint.Properties.UnclampedPosition.Y +
                     ":reach:" + kinectPointerPoint.Properties.HandReachExtent +
                     ":hand:" + kinectPointerPoint.Properties.HandType +
                     ":primary:" + kinectPointerPoint.Properties.IsPrimary;
                javaCom.sendMessage(data);
            }

        }

        /// <summary>
        /// INotifyPropertyChangedPropertyChanged event to allow window controls to bind to changeable data
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        /// <summary>
        /// Gets the bitmap to display
        /// </summary>
        public ImageSource ImageSource
        {
            get
            {
                return this.imageSource;
            }
        }

        /// <summary>
        /// Gets or sets the current status text to display
        /// </summary>
        public string StatusText
        {
            get
            {
                return this.statusText;
            }

            set
            {
                if (this.statusText != value)
                {
                    this.statusText = value;

                    // notify any bound elements that the text has changed
                    if (this.PropertyChanged != null)
                    {
                        this.PropertyChanged(this, new PropertyChangedEventArgs("StatusText"));
                    }
                }
            }
        }

        /// <summary>
        /// Execute start up tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void MainWindow_Loaded(object sender, RoutedEventArgs e)
        {
            if (this.bodyFrameReader != null)
            {
                this.bodyFrameReader.FrameArrived += this.Reader_FrameArrived;
            }

            KinectCoreWindow kinectCoreWindow = KinectCoreWindow.GetForCurrentThread();
            kinectCoreWindow.PointerMoved += kinectCoreWindow_PointerMoved;

        }

        /// <summary>
        /// Execute shutdown tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void MainWindow_Closing(object sender, CancelEventArgs e)
        {
            if (this.bodyFrameReader != null)
            {
                // BodyFrameReader is IDisposable
                this.bodyFrameReader.Dispose();
                this.bodyFrameReader = null;
            }

            if (this.kinectSensor != null)
            {
                this.kinectSensor.Close();
                this.kinectSensor = null;
            }

            if (this.javaCom != null)
            {
                this.javaCom.Dispose();
                this.javaCom = null;
            }

            if (this.speech != null)
            {
                this.speech.cleanup();
                this.speech = null;
            }
        }

        /// <summary>
        /// Handles the body frame data arriving from the sensor
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void Reader_FrameArrived(object sender, BodyFrameArrivedEventArgs e)
        {
            bool dataReceived = false;

            using (BodyFrame bodyFrame = e.FrameReference.AcquireFrame())
            {
                if (bodyFrame != null)
                {
                    if (this.bodies == null)
                    {
                        this.bodies = new Body[bodyFrame.BodyCount];
                    }

                    // The first time GetAndRefreshBodyData is called, Kinect will allocate each Body in the array.
                    // As long as those body objects are not disposed and not set to null in the array,
                    // those body objects will be re-used.
                    bodyFrame.GetAndRefreshBodyData(this.bodies);
                    dataReceived = true;
                }
            }

            if (dataReceived)
            {
                using (DrawingContext dc = this.drawingGroup.Open())
                {
                    // Draw a transparent background to set the render size
                    dc.DrawRectangle(Brushes.Black, null, new Rect(0.0, 0.0, this.displayWidth, this.displayHeight));

                    foreach (Body body in this.bodies)
                    {
                        if (body.IsTracked)
                        {

                            Joint leftHand = body.Joints[JointType.HandLeft];
                            Joint rightHand = body.Joints[JointType.HandRight];

                            if (leftHand.TrackingState == TrackingState.Tracked && rightHand.TrackingState == TrackingState.Tracked)
                            {

                                DepthSpacePoint leftHandDepthSpacePoint = this.coordinateMapper.MapCameraPointToDepthSpace(leftHand.Position);
                                DepthSpacePoint rightHandDepthSpacePoint = this.coordinateMapper.MapCameraPointToDepthSpace(rightHand.Position);
                                Point leftHandPoint = new Point(leftHandDepthSpacePoint.X, leftHandDepthSpacePoint.Y);
                                Point rightHandPoint = new Point(rightHandDepthSpacePoint.X, rightHandDepthSpacePoint.Y);

                                this.DrawHand(body.HandLeftState, leftHandPoint, dc);
                                this.DrawHand(body.HandRightState, rightHandPoint, dc);

                                if (javaCom.isConnected())
                                {
                                    string data =
                                         "lh_state:" + body.HandLeftState +
                                         ":lh_x:" + leftHandPoint.X +
                                         ":lh_y:" + leftHandPoint.Y +
                                         ":rh_state:" + body.HandRightState +
                                         ":rh_x:" + rightHandPoint.X +
                                         ":rh_y:" + rightHandPoint.Y;

                              //      javaCom.sendMessage(data);
                                }

                            }

                        }
                    }

                    // prevent drawing outside of our render area
                    this.drawingGroup.ClipGeometry = new RectangleGeometry(new Rect(0.0, 0.0, this.displayWidth, this.displayHeight));
                }
            }
        }

        /// <summary>
        /// Draws a hand symbol if the hand is tracked: red circle = closed, green circle = opened; blue circle = lasso
        /// </summary>
        /// <param name="handState">state of the hand</param>
        /// <param name="handPosition">position of the hand</param>
        /// <param name="drawingContext">drawing context to draw to</param>
        private void DrawHand(HandState handState, Point handPosition, DrawingContext drawingContext)
        {
            switch (handState)
            {
                case HandState.Closed:
                    drawingContext.DrawEllipse(this.handClosedBrush, null, handPosition, HandSize, HandSize);
                    break;

                case HandState.Open:
                    drawingContext.DrawEllipse(this.handOpenBrush, null, handPosition, HandSize, HandSize);
                    break;

                case HandState.Lasso:
                    drawingContext.DrawEllipse(this.handLassoBrush, null, handPosition, HandSize, HandSize);
                    break;
                case HandState.Unknown:
                    drawingContext.DrawEllipse(this.handUnknownBrush, null, handPosition, HandSize, HandSize);
                    break;
            }
        }


        /// <summary>
        /// Handles the event which the sensor becomes unavailable (E.g. paused, closed, unplugged).
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void Sensor_IsAvailableChanged(object sender, IsAvailableChangedEventArgs e)
        {
            // on failure, set the status text
            this.StatusText = this.kinectSensor.IsAvailable ? Properties.Resources.RunningStatusText
                                                            : Properties.Resources.SensorNotAvailableStatusText;

            if (javaCom != null && !javaCom.isConnected())
            {
                this.StatusText += " and " + Properties.Resources.SocketConnectionError;
            }
        }
    }
}
