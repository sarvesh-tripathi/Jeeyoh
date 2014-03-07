package com.jeeyoh.persistence;

import com.jeeyoh.persistence.domain.Funboard;

public interface IFunBoardDAO {
	
	public void saveFunBoard(Funboard funboard, int batch_size);
	

}
