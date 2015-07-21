package com.example.testvaadin;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.vaadin.viritin.util.BrowserCookie;
import org.vaadin.viritin.util.BrowserCookie.Callback;

import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

/**
 * Use this class to save grid hidden columns to cookies.
 * Use {@link #attachToGrid(Grid, String) }
 *
 */
public class GridUtils {

	private final String SETTINGS_NAME;
	private Grid grid;
	/**
	 * Set specified grid to save hidden columns in cookies.
	 * @param grid - grid which columns would be used
	 * @param cookieName - name of the cookie. Should be unique for every grid.
	 */
	static public void attachToGrid(Grid grid, String cookieName) {
		GridUtils utils=new GridUtils(grid, cookieName);
	}
	private GridUtils(Grid grid, String cookieName) {
		super();
		this.grid=grid;
		SETTINGS_NAME=cookieName;
		loadSettings();
		grid.addColumnVisibilityChangeListener(e->{
			saveSettings();
		});
	}
	
	private void loadSettings() {
		Callback saveFunc=new Callback() {
			@Override
			public void onValueDetected(String value) {
				if(value!=null) {
					String[] hiddenColumns=value.split(":");
					Arrays.stream(hiddenColumns).forEach(col -> {
						Column column = grid.getColumn(col);
						if(column!=null) {
							column.setHidden(true);
						}
					});
				}
			}
		};
		BrowserCookie.detectCookieValue(SETTINGS_NAME, saveFunc);
	}
	
	private void saveSettings() {
		Optional<String> value = grid.getColumns().stream().filter(c -> c.isHidden())
				.map(a -> a.getPropertyId().toString())
				.reduce((a, b) -> a + ":" + b);

		if(value.isPresent()) {
			BrowserCookie.setCookie(SETTINGS_NAME, value.get());
		}
		else {
			BrowserCookie.setCookie(SETTINGS_NAME, "");
		}
	}
	

}
