package com.conx.logistics.reporting.remote.services;

import java.io.OutputStream;
import java.util.Map;

public interface IReportGenerator {
	public OutputStream generatePDF(String reportName, Map<String,Object> params) throws Exception;
	
	public void generatePDFToFile(String reportName, Map<String,Object> params, String outputFileName) throws Exception;
}
