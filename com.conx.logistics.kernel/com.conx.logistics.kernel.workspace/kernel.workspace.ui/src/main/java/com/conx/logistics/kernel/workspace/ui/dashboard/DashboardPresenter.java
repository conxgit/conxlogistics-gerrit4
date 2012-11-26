package com.conx.logistics.kernel.workspace.ui.dashboard;

import javax.persistence.EntityManager;

import org.jbpm.task.query.TaskSummary;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.conx.logistics.kernel.system.dao.services.application.IFeatureDAOService;
import com.conx.logistics.kernel.ui.common.mvp.MainEventBus;
import com.conx.logistics.kernel.ui.common.mvp.MainMVPApplication;
import com.conx.logistics.kernel.workspace.provider.NamedQueryLocalEntityProvider;
import com.conx.logistics.kernel.workspace.ui.dashboard.view.DashboardView;
import com.conx.logistics.kernel.workspace.ui.dashboard.view.IDashboardView;
import com.conx.logistics.mdm.domain.application.Feature;
import com.conx.logistics.mdm.domain.user.User;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@Presenter(view = DashboardView.class)
public class DashboardPresenter extends FactoryPresenter<IDashboardView, DashboardEventBus> {

	private EntityManager humanTaskEM;
	private JPAContainer<TaskSummary> taskContainer;
	private User currentUser;

	private boolean isLaunched = false;
	private MainMVPApplication mainApp;

	@Override
	public void bind() {
		getView().init();
		// ((DashboardView)getView()).getMyTasksTable()
	}

	public void onLaunch(MainMVPApplication mainApp) {
		if (!isLaunched) {
			this.mainApp = mainApp;
			this.currentUser = mainApp.getCurrentUser();

			// -- Configure Tasks
			this.humanTaskEM = mainApp.getEntityManagerFactoryManager().getHumanTaskEmf().createEntityManager();

			/*
			 * this.taskContainer =
			 * JPAContainerFactory.make(TaskSummary.class,this.humanTaskEM);
			 * this.taskContainer =
			 * JPAContainerFactory.make(Task.class,this.humanTaskEM);
			 * this.taskContainer
			 * .addNestedContainerProperty("taskData.actualOwner.id");
			 * this.taskContainer
			 * .addNestedContainerProperty("taskData.createdOn");
			 * this.taskContainer
			 * .addNestedContainerProperty("taskData.processId");
			 * this.taskContainer
			 * .addNestedContainerProperty("taskData.processInstanceId");
			 * 
			 * ((DashboardView)getView()).getMyTasksTable().getFilterTable().
			 * setContainerDataSource(this.taskContainer);
			 * 
			 * ((DashboardView)getView()).getMyTasksTable().getFilterTable().
			 * setColumnHeader("id", "ID");
			 * ((DashboardView)getView()).getMyTasksTable
			 * ().getFilterTable().setColumnHeader("taskData.createdOn",
			 * "Created");
			 * ((DashboardView)getView()).getMyTasksTable().getFilterTable
			 * ().setColumnHeader("taskData.actualOwner.id", "Owner");
			 * ((DashboardView
			 * )getView()).getMyTasksTable().getFilterTable().setColumnHeader
			 * ("taskData.processId", "Process");
			 * ((DashboardView)getView()).getMyTasksTable
			 * ().getFilterTable().setColumnHeader("taskData.processInstanceId",
			 * "Process Instance ID");
			 * 
			 * ((DashboardView)getView()).getMyTasksTable().getFilterTable().
			 * setVisibleColumns(new
			 * String[]{"id","taskData.createdOn","taskData.actualOwner.id"
			 * ,"taskData.processId","taskData.processInstanceId"});
			 */

			this.taskContainer = new JPAContainer<TaskSummary>(TaskSummary.class);
			NamedQueryLocalEntityProvider namedQueryProvider = new NamedQueryLocalEntityProvider(TaskSummary.class, "TasksOwnedWithNulls",
					this.humanTaskEM);
			namedQueryProvider.addParameter("language", "en-UK");
			namedQueryProvider.addParameter("userId", this.currentUser.getScreenName());
			this.taskContainer.setEntityProvider(namedQueryProvider);

			this.taskContainer.addNestedContainerProperty("actualOwner.id");

			((DashboardView) getView()).getMyTasksTable().getFilterTable().setContainerDataSource(this.taskContainer);

			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("id", "ID");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("name", "Task");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("status", "Status");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("comment", "Description");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("createdOn", "Created");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("actualOwner.id", "Owner");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("processId", "Process");
			((DashboardView) getView()).getMyTasksTable().getFilterTable().setColumnHeader("processInstanceId", "Process Instance ID");

			((DashboardView) getView())
					.getMyTasksTable()
					.getFilterTable()
					.setVisibleColumns(
							new String[] { "id", "name", "status", "comment", "createdOn", "actualOwner.id", "processId", "processInstanceId" });

			this.isLaunched = true;

			/*
			 * this.taskContainer.getEntityProvider().setQueryModifierDelegate(new
			 * DefaultQueryModifierDelegate() {
			 * 
			 * @Override public void queryWillBeBuilt(CriteriaBuilder
			 * criteriaBuilder, javax.persistence.criteria.CriteriaQuery<?>
			 * query) {
			 * 
			 * };
			 * 
			 * @Override public void filtersWillBeAdded(CriteriaBuilder
			 * criteriaBuilder, CriteriaQuery<?> query, List<Predicate>
			 * predicates) {
			 * 
			 * Root<?> taskRoot = query.getRoots().iterator().next();
			 * 
			 * // Add a "WHERE age > 116" expression Path<String>
			 * taskOwnerUserId = taskRoot.<org.jbpm.task.User>
			 * get("actualOwner").get("id");
			 * predicates.add(criteriaBuilder.equal(taskOwnerUserId,
			 * DashboardPresenter.this.currentUser.getScreenName())); } });
			 */

			this.taskContainer.applyFilters();

			((DashboardView) getView()).getMyTasksTable().getFilterTable().addListener(new ItemClickListener() {
				@Override
				public void itemClick(ItemClickEvent event) {
					if (event.isDoubleClick()) {
						EntityItem<TaskSummary> ei = (EntityItem<TaskSummary>) event.getItem();
						TaskSummary ts = ei.getEntity();
						Feature taskFeature = DashboardPresenter.this.mainApp.getDaoProvider().provideByDAOClass(IFeatureDAOService.class).findFeatureByCode(ts.getProcessId());
						taskFeature.setTaskId(Long.toString(ts.getId()));
						((MainEventBus) DashboardPresenter.this.mainApp.getMainPresenter().getEventBus()).onOpenApplicationFeature(taskFeature);
					}
				}
			});
		}
	}
}
