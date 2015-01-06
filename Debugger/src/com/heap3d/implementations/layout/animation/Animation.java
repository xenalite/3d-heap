package com.heap3d.implementations.layout.animation;

import com.graphics.shapes.Shape;
import com.heap3d.implementations.snapshot.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Animation implements IAnimation {

	private boolean alreadyDone;
	private List<AnimationEvent> deleteEvents = new ArrayList<>();
	private List<AnimationEvent> moveEvents = new ArrayList<>();
	private List<AnimationEvent> addEvents = new ArrayList<>();

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

	private void createEvents(Set<Entry<Node, Shape>> startingEntries, Set<Entry<Node, Shape>> finishingEntries) {
		for (Entry<Node,Shape> fromEntry : startingEntries) {
			boolean moveAnimation = false;
			for (Entry<Node,Shape> toEntry : finishingEntries) {
				if (toEntry.getKey().equals(fromEntry.getKey())) {
					moveAnimation = true;
					moveEvents.add(new MoveAnimation(fromEntry.getValue(), toEntry.getValue()));
					break;
				}
			}

			if (!moveAnimation) {
				deleteEvents.add(new DeleteAnimation(fromEntry.getValue()));
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
			}
		}

	}
}
