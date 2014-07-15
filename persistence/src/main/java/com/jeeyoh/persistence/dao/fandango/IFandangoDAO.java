package com.jeeyoh.persistence.dao.fandango;

import com.jeeyoh.persistence.domain.FandangoComingSoon;
import com.jeeyoh.persistence.domain.FandangoNearMe;
import com.jeeyoh.persistence.domain.FandangoOpening;
import com.jeeyoh.persistence.domain.FandangoTopTen;

public interface IFandangoDAO {
	public void saveTopTen(FandangoTopTen fandango);

	void saveCommingSoon(FandangoComingSoon fandango);

	void saveNewOpning(FandangoOpening fandango);

	public void saveMovieNearMe(FandangoNearMe fandango);

}
