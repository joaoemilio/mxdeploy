package com.mxdeploy.swt.project.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.ProjectServer;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.api.service.ServerService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.AddServerTreeItemEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxdeploy.swt.project.ServerProjectComposite;

public class AddServerProjectHelper {
	// static Logger logger = Logger.getLogger(AddServerProjectHelper.class);

	public ServerProjectComposite composite = null;

	private String[] itens = { "HOSTNAME", "NAME" };

	private final int COLUMN_HOSTNAME = 0;
	private final int COLUMN_NAME = 1;

	public AddServerProjectHelper(ServerProjectComposite composite) {
		this.composite = composite;
	}

	public void loadComboBox() {
		composite.columnSearchCCombo.setItems(itens);
		composite.columnSearchCCombo.select(0);
	}

	public void close() {
		Shell shell = (Shell) composite.getParent();
		shell.close();
		shell.dispose();
	}

	public void search() {
		int index = composite.columnSearchCCombo.getSelectionIndex();
		// WorkingSet ws = workingSetHelper.getWorkingSetSelected();

		Server objectValue = new Server();
		// objectValue.setIdWorkingSet(ws.getId());
		ServerService service = new ServerService();

		if ((composite.nameSearchText.getText() != null)
				&& (composite.nameSearchText.getText().trim().length() > 0)) {
			if (!composite.nameSearchText.getText().trim().equals("*")) {
				switch (index) {
				case COLUMN_NAME:
					objectValue.setName(composite.nameSearchText.getText());
					break;
				default:
					objectValue.setHostname(composite.nameSearchText.getText());
					break;
				}
			}
		}

		try {
			List<Server> list = service.search(objectValue);
			if ((list != null) && (!list.isEmpty())) {
				for (Server server : list) {
					addItemResultTable(server);
				}
				composite.resultServerTable.select(0);
			}
		} catch (ServiceException e) {
			// logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK
					| SWT.ICON_ERROR);
			e.printStackTrace();
		}

	}

	public void addItemResultTable(Server server) {
		if (!isServerIncluded(server)) {
			TableItem tableItem = new TableItem(composite.resultServerTable,
					SWT.NONE);
			tableItem.setText(server.getHostname());
			tableItem.setData(server);
			tableItem.setImage(Constant.IMAGE_SERVER);
		}
	}

	public List<Server> getServerAllInResultServerTable() {
		TableItem[] tableItem = composite.resultServerTable.getSelection();
		Server server = null;
		List<Server> list = new ArrayList<Server>();
		for (int i = 0; i < tableItem.length; i++) {
			server = (Server) tableItem[i].getData();
			list.add(server);
		}
		return list;
	}

	public List<Server> getServerAllInServerSelectedTable() {
		TableItem[] tableItem = composite.serverSelectedTable.getItems();
		Server server = null;
		List<Server> list = new ArrayList<Server>();
		for (int i = 0; i < tableItem.length; i++) {
			server = (Server) tableItem[i].getData();
			list.add(server);
		}
		return list;
	}

	public void addServerListInServerSeletedTable(List<Server> list) {
		if ((list != null) && (!list.isEmpty())) {
			for (Server server : list) {
				if (!isServerIncluded(server)) {
					TableItem tableItem = new TableItem(
							composite.serverSelectedTable, SWT.NONE);
					tableItem.setText(server.getHostname());
					tableItem.setData(server);
					tableItem.setImage(Constant.IMAGE_SERVER);
				}
			}
		}
	}

	public void addItemServerSelectedTable() {
		List<Server> list = getServerAllInResultServerTable();
		if ((list != null) && (!list.isEmpty())) {
			addServerListInServerSeletedTable(list);
			removeItemResultTable();
		}
	}

	public boolean isServerIncluded(Server server) {
		TableItem[] itemIncluded = composite.serverSelectedTable.getItems();
		for (int i = 0; i < itemIncluded.length; i++) {
			Server serverIncluded = (Server) itemIncluded[i].getData();
			if (serverIncluded.getId().equals(server.getId())) {
				return true;
			}
		}
		return false;
	}

	public void removeItemServerSelectedTable() {
		int[] indexIncluded = composite.serverSelectedTable
				.getSelectionIndices();
		composite.serverSelectedTable.remove(indexIncluded);
	}

	public void removeItemResultTable() {
		int[] indexIncluded = composite.resultServerTable.getSelectionIndices();
		composite.resultServerTable.remove(indexIncluded);
	}

	public void save() {
		List<Server> serverListInTable = getServerAllInServerSelectedTable();
		ControlPanelHelper controlPanelHelper = MainShell.getControlPanelHelper();
		Project project = controlPanelHelper.getProjectSelectedInMyProjectTree();

		ProjectService service = new ProjectService();

		ServerService serverService = new ServerService();
		List<Server> serverListInDB;
		try {
			serverListInDB = serverService.searchByProject(project.getId());
		} catch (ServiceException e) {
			// logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK
					| SWT.ICON_ERROR);
			e.printStackTrace();
			return;
		}

		if (serverListInTable != null && !serverListInTable.isEmpty()) {
			for (Server serverInTable : serverListInTable) {
				boolean existInDb = false;
				if (serverListInDB != null && !serverListInDB.isEmpty()) {
					for (Server serverInDb : serverListInDB) {
						if (serverInDb.getId().equals(serverInTable.getId())) {
							existInDb = true;
							break;
						}
					}
				}
				if (!existInDb) {
					ProjectServer projectServer = new ProjectServer();
					projectServer.setIdServer(serverInTable.getId());
					projectServer.setIdProject(project.getId());

					service.createProjectServer(project, serverInTable);
				}

			}
		}

		if (serverListInDB != null && !serverListInDB.isEmpty()) {

			List<Server> serverListInDBClose = new ArrayList<Server>();
			for (Server serverInDb : serverListInDB) {
				serverListInDBClose.add(serverInDb);
			}

			for (Server serverInDb : serverListInDBClose) {
				boolean existInDb = false;
				if (serverListInTable != null && !serverListInTable.isEmpty()) {
					for (Server serverInTable : serverListInTable) {
						if (serverInDb.getId().equals(serverInTable.getId())) {
							existInDb = true;
							break;
						}
					}
				}

				if (!existInDb) {
					ProjectServer projectServer = new ProjectServer();
					projectServer.setIdServer(serverInDb.getId());
					projectServer.setIdProject(project.getId());

					service.removerProjectServer(projectServer);
				}

			}
		}

	}

	public void loadCreatedProject() {
		ControlPanelHelper controlPanelHelper = MainShell.getControlPanelHelper();
		Project project = controlPanelHelper.getProjectSelectedInMyProjectTree();
		ServerService serverService = new ServerService();
		List<Server> listServer;
		try {
			listServer = serverService.searchByProject(project.getId());
			if ((listServer != null) && (!listServer.isEmpty())) {
				addServerListInServerSeletedTable(listServer);
			}
		} catch (ServiceException e) {
			// logger.error(e.getMessage(),e);
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK
					| SWT.ICON_ERROR);
			e.printStackTrace();
		}

	}

	public void refreshTree() {
		List<Server> list = getServerAllInServerSelectedTable();
		// workingSetHelper.removeTreeItemServerOfProject();
		ControlPanelHelper controlPanelHelper = MainShell.getControlPanelHelper();
		TreeItem treeItem = controlPanelHelper.getMyProjectTreeItemSelected();
		TreeItem[] serversTreeItem = treeItem.getItems();

		for (int i = 0; serversTreeItem.length > i; i++) {
			boolean existInDb = false;
			if (!((TreeItem) serversTreeItem[i]).isDisposed()) {
				if (((TreeItem) serversTreeItem[i]).getData() instanceof Server) {
					Server serverTreeItem = (Server) ((TreeItem) serversTreeItem[i])
							.getData();
					if (list != null && !list.isEmpty()) {
						for (Server server : list) {
							if (serverTreeItem.getId().equals(server.getId())) {
								existInDb = true;
								break;
							}
						}
						if (!existInDb) {
							((TreeItem) serversTreeItem[i]).dispose();
						}
					}
				}
			}
		}

		if (list != null && !list.isEmpty()) {
			for (Server server : list) {
				boolean existInDb = false;
				for (int i = 0; serversTreeItem.length > i; i++) {
					if (!((TreeItem) serversTreeItem[i]).isDisposed()) {
						if (((TreeItem) serversTreeItem[i]).getData() instanceof Server) {
							Server serverTreeItem = (Server) ((TreeItem) serversTreeItem[i])
									.getData();
							if (serverTreeItem.getId().equals(server.getId())) {
								existInDb = true;
								break;
							}
						}
					}
				}
				if (!existInDb) {
					AddServerTreeItemEventHandler.execute(treeItem, server);
				}
			}
		}
	}

}
