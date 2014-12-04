package com.imperial.heap3d.implementations.layout.animation;

public abstract class AnimationEvent {

	protected int maxIterations = 120;
	private int iteration = 0;

	public AnimationEvent(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public AnimationEvent() {}

	public void step() {
		if(!isFinished())
			executeStep();
		++iteration;
	}

	protected abstract void executeStep();

	public abstract void finish();

	public boolean isFinished() {
		return iteration >= maxIterations;
	}
}