package com.example.Board.handelr;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.Board.event.AfterTransactionEvent;
import com.example.Board.event.BeforeTransactionEvent;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class MemberEventHandler {
	
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void beforTransactionProcess(BeforeTransactionEvent beforeTransactionEvent) {
		beforeTransactionEvent.callback();
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void afterTransactionProcess(AfterTransactionEvent afterTransactionEvent) {
		afterTransactionEvent.callback();
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void completedTransactionProcess(AfterTransactionEvent afterTransactionEvent) {
		afterTransactionEvent.completed();
	}
	
	
}
