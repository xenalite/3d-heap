***********
* 3D Heap *
***********

Installing Ant
________________

WINDOWS

1. Download binarys from: apache-ant-1.9.4-bin.zip
2. Uncompress the downloaded file into a directory.
3. Set environmental variables JAVA_HOME to your Java environment, ANT_HOME to the directory you uncompressed Ant to, and add ${ANT_HOME}/bin (Unix) or %ANT_HOME%/bin (Windows) to your PATH. See Setup for details.
4. from the ANT_HOME directory run: 'ant -f fetch.xml -Ddest=system' to get the library dependencies of most of the Ant tasks that require them

LINUX

1. Run sudo apt-get install ant