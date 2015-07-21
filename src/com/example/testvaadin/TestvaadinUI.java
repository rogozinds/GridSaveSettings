package com.example.testvaadin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import org.vaadin.viritin.util.BrowserCookie;
import org.vaadin.viritin.util.BrowserCookie.Callback;

import com.example.testvaadin.models.DataObject;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("testvaadin")
public class TestvaadinUI extends UI {

	private static final String SETTINGS_NAME = "mySettings";

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = TestvaadinUI.class, widgetset = "com.example.testvaadin.widgetset.TestvaadinWidgetset")
	public static class Servlet extends VaadinServlet {
	}


	Grid table;
	private BeanItemContainer<DataObject> ds = new BeanItemContainer<DataObject>(
			DataObject.class);

	@Override
	protected void init(VaadinRequest request) {

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		Label info =new Label();
		info.setValue("Hide columns. Grid will save hidden columns in the browser cookies.");
		ds.addBean(new DataObject("1", "Name", "LastName"));
		ds.addBean(new DataObject("2", "Name2", "LastName2"));
		ds.addBean(new DataObject("3", "Name3", "LastName3"));
		table = new Grid(SETTINGS_NAME);
		table.setContainerDataSource(ds);
		makeGridColumnsHideable(table);
		GridUtils.attachToGrid(table,"userGrid");
		setContent(layout);
		layout.addComponent(info);
		layout.addComponent(table);

	}
	
	private void makeGridColumnsHideable(Grid grid) {
		grid.getColumns().forEach(c->{
			c.setHidable(true);
		});
	}
}