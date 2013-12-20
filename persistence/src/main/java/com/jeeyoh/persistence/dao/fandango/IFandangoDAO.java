package com.jeeyoh.persistence.dao.fandango;

import com.jeeyoh.persistence.domain.FandangoCommingSoon;
import com.jeeyoh.persistence.domain.FandangoNearMe;
import com.jeeyoh.persistence.domain.FandangoOpenning;
import com.jeeyoh.persistence.domain.FandangoTopTen;

public interface IFandangoDAO {
	public void saveTopTen(FandangoTopTen fandango);

	void saveCommingSoon(FandangoCommingSoon fandango);

	void saveNewOpning(FandangoOpenning fandango);

	public void saveMovieNearMe(FandangoNearMe fandango);

}
