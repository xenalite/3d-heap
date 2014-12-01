package com.imperial.heap3d.layout.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.imperial.heap3d.snapshot.Node;

public class Animate {

	private Set<Node> fromNodes;
	private Set<Node> toNodes;
	private List<AnimationEvent> deleteEvents, moveEvents, addEvents;
	
	public Animate(Set<Node> allHeapNodes){
		this.fromNodes = allHeapNodes;
	}

	public void setToStackNodes(Set<Node> allHeapNodes) {
		this.toNodes = allHeapNodes;
		deleteEvents = new ArrayList<AnimationEvent>();
		moveEvents = new ArrayList<AnimationEvent>();
		addEvents = new ArrayList<AnimationEvent>();
		
		makeEvents();
	}
	
	private void makeEvents() {
		for(Node fromNode : fromNodes){
			boolean moveAnimation = false;
			for(Node toNode : toNodes){
				if(toNode.equals(fromNode)){
					moveAnimation = true;
					moveEvents.add(new MoveAnimation(fromNode, toNode));
					break;
				}
			}
			
			if(!moveAnimation){
				// It has been deleted!
				deleteEvents.add(new DeleteAnimation(fromNode));
			}
		}
		
		for(Node toNode : toNodes){
			boolean moveAnimation = false;
			for(Node fromNode : fromNodes){
				if(toNode.equals(fromNode)){
					moveAnimation = true;
				}
			}
			
			if(!moveAnimation){
				// It has been added!
				addEvents.add(new AddAnimation(toNode));
			}
		}
		
	}

	public boolean runAnimation(){
		
		boolean done = true;
		
		if(!deleteEvents.isEmpty() && !deleteEvents.get(0).finishedAnimation()){
			for(AnimationEvent deleted : deleteEvents)
				deleted.step();
			done = false;
		}
		
		if(!moveEvents.isEmpty() && !moveEvents.get(0).finishedAnimation()){
			for(AnimationEvent move : moveEvents)
				move.step();
			done = false;
		}

		if(!addEvents.isEmpty() && !addEvents.get(0).finishedAnimation()){
			for(AnimationEvent newEvents : addEvents)
				newEvents.step();
			done = false;
		}
		
		return done;
	}
}
