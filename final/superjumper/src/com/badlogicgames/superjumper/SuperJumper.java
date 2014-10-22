/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.nextpeer.libgdx.NextpeerPlugin;
import com.nextpeer.libgdx.Tournaments;
import com.nextpeer.libgdx.TournamentsCallback;

public class SuperJumper extends Game{
	boolean firstTimeCreate = true;
	FPSLogger fps;
	
	public SuperJumper() {
		this(null);
	}
	
	public SuperJumper(Tournaments tournaments) {
		
		// If we have a supported tournaments object, set the game as callback
		if (tournaments != null && tournaments.isSupported()) {
			tournaments.setTournamentsCallback(mNextpeerTournamentsCallback);
			NextpeerPlugin.load(tournaments);
		}
	}
	
	@Override
	public void create () {
		Settings.load();
		Assets.load();

		setScreen(new MainMenuScreen(this));
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	}

	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();

		getScreen().dispose();
	}
	
	/**
	 * TournamentsCallback implementation
	 * Responsible to answer on certain tournament events such as start tournament & end tournament.
	 */
    private TournamentsCallback mNextpeerTournamentsCallback = new TournamentsCallback() {
		
		@Override
		public void onTournamentStart(long tournamentRandomSeed) {
	        // Start the game scene
	        NextpeerPlugin.instance().lastKnownTournamentRandomSeed = tournamentRandomSeed;
	    	setScreen(new GameScreen(SuperJumper.this));
		}
		
		@Override
		public void onTournamentEnd() {
	        // End the game scene, switch to main menu
	        NextpeerPlugin.instance().lastKnownTournamentRandomSeed = 0;
	    	setScreen(new MainMenuScreen(SuperJumper.this));
		}
	};
}