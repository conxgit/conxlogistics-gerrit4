package com.conx.logistics.kernel.pageflow.engine.path;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.definition.process.Node;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conx.logistics.kernel.pageflow.services.PageFlowPage;

public class PageFlowPathAssessor {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String name;
	private List<Node> nodePath;
	private Map<String,List<Node>> possibleNextPaths;
	
	private List<PageFlowPage> currentOrderedPageList;
	
	private Map<String,PageFlowPage> pageCache;
	private PageFlowPage currentPage;
	private long currentTaskId;
	private boolean pagesChanged;
	
	public PageFlowPathAssessor(){
	}
	
	public PageFlowPathAssessor(
			    String name, 
				List<Node> nodePath,
			    Map<String, List<Node>> possibleNextPaths,
			    Map<String,PageFlowPage> pageCache) {
		super();
		this.name = name;
		this.nodePath = nodePath;
		this.possibleNextPaths = possibleNextPaths;
		this.pageCache = pageCache;
		
		createOrderedPageList();//Use nodeList for TS
		updateCurrentPageInfo(null);
	}
	
	public boolean restActivePages(TaskSummary ts)
	{
		this.currentTaskId = ts.getId();
		String name_ = null;
		List<Node> nodePath_ = null;
		
		try {
			//Find next path
			for (String pathKey : possibleNextPaths.keySet())
			{
				if (pathKey.startsWith(name+"-->"+ts.getName()))//Partial path
				{
					name_ = pathKey;
					nodePath_ = possibleNextPaths.get(pathKey);
					break;
				}
				else if (pathKey.equalsIgnoreCase(name) && pathKey.endsWith("End"))//Complete path
				{
					name_ = pathKey;
					nodePath_ = possibleNextPaths.get(pathKey);
					break;				
				}
			}
			
			if (name != name_)
				this.pagesChanged = true;
			else
				this.pagesChanged = false;
			
			
			name = name_;
			nodePath = nodePath_;
			
			if (this.pagesChanged)
			{
				createOrderedPageList();
			}
			
			updateCurrentPageInfo(ts.getName());
		}
		catch(Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.error(stacktrace);				 
			throw new RuntimeException("restActivePages() failed.",e);
		}
		
		return this.pagesChanged;
	}

	private void createOrderedPageList() {
		//Create currentOrderedPageList
		this.currentOrderedPageList = new ArrayList<PageFlowPage>();
		for (Node node : nodePath)
		{
			if (node instanceof HumanTaskNode)
			{
				this.currentOrderedPageList.add(pageCache.get(node.getName()));
			}
		}
	}
	
	private void updateCurrentPageInfo(String currentTaskName) {
		//Create currentOrderedPageList
		this.currentOrderedPageList = new ArrayList<PageFlowPage>();
		String firstTaskName = null;
		for (Node node : nodePath)
		{
			if (node instanceof HumanTaskNode)
			{
				this.currentOrderedPageList.add(pageCache.get(node.getName()));
				if (currentTaskName == null && firstTaskName == null)
				{
					firstTaskName = node.getName();
					this.currentTaskId = ((HumanTaskNode)node).getId();
					currentPage = pageCache.get(node.getName());
				}
				else if (currentTaskName.equalsIgnoreCase(node.getName()))
				{
					currentPage = pageCache.get(currentTaskName);
				}
			}
		}
	}	

	public String getName() {
		return name;
	}

	public List<Node> getNodePath() {
		return nodePath;
	}

	public Map<String, List<Node>> getPossibleNextPaths() {
		return possibleNextPaths;
	}

	public List<PageFlowPage> getCurrentOrderedPageList() {
		return currentOrderedPageList;
	}

	public Map<String, PageFlowPage> getPageCache() {
		return pageCache;
	}

	public boolean isPagesChanged() {
		return pagesChanged;
	}

	public PageFlowPage getCurrentPage() {
		return currentPage;
	}

	public long getCurrentTaskId() {
		return currentTaskId;
	}
}
