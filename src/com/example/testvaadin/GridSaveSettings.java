package com.example.testvaadin;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.vaadin.viritin.util.BrowserCookie;
import org.vaadin.viritin.util.BrowserCookie.Callback;

import com.vaadin.data.Container.Indexed;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

public class GridSaveSettings extends Grid {

	private final String SETTINGS_NAME;
	public GridSaveSettings(String cookieName) {
		super();
		SETTINGS_NAME=cookieName;
		
	}
	@Override
	public void setContainerDataSource(Indexed container) {
		super.setContainerDataSource(container);
		getColumns().forEach(column -> {
			column.setHidable(true);
		});
		loadSettings();
		addColumnVisibilityChangeListener(e->{
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
						Column column = getColumn(col);
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
		Optional<String> value = getColumns().stream().filter(c -> c.isHidden())
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
