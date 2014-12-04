package com.imperial.heap3d.implementations.layout.animation;

import com.imperial.heap3d.implementations.snapshot.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Animate {

	private boolean finished;
	private Set<Node> startingNodes;
	private Set<Node> finishingNodes;
	private Runnable finishingCommand;
	private List<AnimationEvent> deleteEvents = new ArrayList<>();
	private List<AnimationEvent> moveEvents = new ArrayList<>();
	private List<AnimationEvent> addEvents = new ArrayList<>();

	public void initialise(Set<Node> startingNodes, Set<Node> finishingNodes, Runnable finishingCommand) {
		finish();
		finished = false;
		this.startingNodes = startingNodes;
		this.finishingNodes = finishingNodes;
		this.finishingCommand = finishingCommand;

		createEvents();
	}

	private void finish() {
		while (!finished)
			step();
		cleanUp();
	}

	public boolean step() {
		if (finished)
			return true;

		boolean isAnimationFinished = true;
		isAnimationFinished &= step(deleteEvents);
		isAnimationFinished &= step(moveEvents);
		isAnimationFinished &= step(addEvents);
		finished = isAnimationFinished;
		if(finished)
			finishingCommand.run();
		return isAnimationFinished;
	}

	private boolean step(List<AnimationEvent> events) {
		if (!events.isEmpty() && !events.get(0).isFinished()) {
			for (AnimationEvent event : events)
				event.step();
			return false;
		}
		return true;
	}

	private void cleanUp() {
		startingNodes = null;
		finishingNodes = null;
		deleteEvents.clear();
		moveEvents.clear();
		addEvents.clear();
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
