package com.heap3d.implementations.animation;

import com.graphics.shapes.Shape;
import com.heap3d.implementations.node.Node;
import com.heap3d.interfaces.animation.IAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Animation implements IAnimation {

	private boolean alreadyDone;
	private List<AnimationEvent> deleteEvents = new ArrayList<>();
	private List<AnimationEvent> moveEvents = new ArrayList<>();
	private List<AnimationEvent> addEvents = new ArrayList<>();
	private boolean anythingChanged = false;
	
	public Animation(Set<Entry<Node,Shape>> startingNodes, Set<Entry<Node,Shape>> finishingNodes) {
		alreadyDone = false;
		createEvents(startingNodes, finishingNodes);
	}

	@Override
	public boolean executeStepAndCheckIfDone() {
		if (alreadyDone)
			return true;

		boolean isAnimationFinished = true;
		isAnimationFinished &= step(deleteEvents);
		isAnimationFinished &= step(moveEvents);
		isAnimationFinished &= step(addEvents);
		alreadyDone = isAnimationFinished;
		return isAnimationFinished;
	}

	@Override
	public void finalise() {
		for(AnimationEvent event : deleteEvents)
			event.finish();

		for(AnimationEvent event : addEvents)
			event.finish();

		for(AnimationEvent event : moveEvents)
			event.finish();
	}

	private boolean step(List<AnimationEvent> events) {
		if (!events.isEmpty() && !events.get(0).isFinished()) {
			for (AnimationEvent event : events)
				event.step();
			return false;
		}
		return true;
	}
	
	private boolean createEvents(Set<Entry<Node, Shape>> startingEntries, Set<Entry<Node, Shape>> finishingEntries) {
		anythingChanged = false;
		for (Entry<Node,Shape> fromEntry : startingEntries) {
			boolean moveAnimation = false;
			for (Entry<Node,Shape> toEntry : finishingEntries) {
				if (toEntry.getKey().equals(fromEntry.getKey())) {
					moveAnimation = true;
					Shape from = fromEntry.getValue();
					Shape to = toEntry.getValue();
					if(!samePosition(from.getPosition(), to.getPosition())){
						moveEvents.add(new MoveAnimation(fromEntry.getValue(), toEntry.getValue()));
						anythingChanged = true;
					}
					break;
				}
			}

			if (!moveAnimation) {
				deleteEvents.add(new DeleteAnimation(fromEntry.getValue()));
				anythingChanged = true;
			}
		}

		for (Entry<Node,Shape> toEntry : finishingEntries) {
			boolean moveAnimation = false;
			for (Entry<Node,Shape> fromEntry : startingEntries) {
				if (toEntry.getKey().equals(fromEntry.getKey())) {
					moveAnimation = true;
				}
			}

			if (!moveAnimation) {
				addEvents.add(new AddAnimation(toEntry.getValue()));
				anythingChanged = true;
			}
		}
		return anythingChanged;
	}
	
	private boolean samePosition(float[] a, float[] b){
		return a[0] == b[0] && a[1] == b[1] && a[2] == b[2];
	}
	
	public boolean hasAnythingChanged(){
		return anythingChanged;
	}
	
	public boolean hasMoveEvents(){
		return !moveEvents.isEmpty();
	}
}
