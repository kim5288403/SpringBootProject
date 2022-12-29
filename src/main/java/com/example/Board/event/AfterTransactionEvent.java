package com.example.Board.event;

public interface AfterTransactionEvent extends AbstractTransactionEvent{
	void completed();
}
