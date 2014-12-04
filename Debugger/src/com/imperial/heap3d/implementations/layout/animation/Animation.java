package com.imperial.heap3d.implementations.layout.animation;

import com.imperial.heap3d.implementations.snapshot.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Animation implements IAnimation {

	private boolean alreadyDone;
	private Set<Node> startingNodes;
	private Set<Node> finishingNodes;
	private List<AnimationEvent> deleteEvents = new ArrayList<>();
	private List<AnimationEvent> moveEvents = new ArrayList<>();
	private List<AnimationEvent> addEvents = new ArrayList<>();

	public Animation(Set<Node> startingNodes, Set<Node> finishingNodes) {
		this.startingNodes = startingNodes;
		this.finishingNodes = finishingNodes;
		alreadyDone = false;
		createEvents();
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

	private void createEvents() {
		for (Node fromNode : startingNodes) {
			boolean moveAnimation = false;
			for (Node toNode : finishingNodes) {

				if (toNode.equals(fromNode)) {
					moveAnimation = true;
					moveEvents.add(new MoveAnimation(fromNode, toNode));
					break;
				}
			}

			if (!moveAnimation) {
				deleteEvents.add(new DeleteAnimation(fromNode));
			}
		}

		for (Node toNode : finishingNodes) {
			boolean moveAnimation = false;
			for (Node fromNode : startingNodes) {
				if (toNode.equals(fromNode)) {
					moveAnimation = true;
				}
			}

			if (!moveAnimation) {
				addEvents.add(new AddAnimation(toNode));
			}
		}

	}
}
