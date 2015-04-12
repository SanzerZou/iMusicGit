package com.gcc.imusic.vtuner;

import java.util.List;

import android.content.Context;

import com.android.vtuner.async.LoadingAndParsingTask;
import com.android.vtuner.async.LoadingAndParsingTask.LoadingParsingCallback;
import com.android.vtuner.async.TokenLoadingTask;
import com.android.vtuner.data.BaseModel;
import com.android.vtuner.data.Directory;
import com.android.vtuner.data.ShowOnDemand;
import com.android.vtuner.utils.UrlBuilder;

public class VtunerMediaServer {
	
	private String friendlyName;
	private List<BaseModel> models;
	
	public void loadingAndParsing(Context context, LoadingParsingCallback loadingParsingCallback, BaseModel model){
		if (model instanceof Directory) {
			LoadingAndParsingTask loadingAndParsingTask = new LoadingAndParsingTask(loadingParsingCallback, "", false);
			UrlBuilder ub = UrlBuilder.getInstance();
			ub.setRequestParameters(context); 
			String url = ub.getRequestUrlAndr(((Directory)model).getUrl());
			String backUpUrl = ub.getRequestUrlAndr(((Directory)model).getUrlBackUp());
			loadingAndParsingTask.execute(url, backUpUrl);
		}else if (model instanceof ShowOnDemand) {
			LoadingAndParsingTask loadingAndParsingTask = new LoadingAndParsingTask(loadingParsingCallback, "", false);
			UrlBuilder ub = UrlBuilder.getInstance();
			ub.setRequestParameters(context); 
			String url = ub.getRequestUrlAndr(((ShowOnDemand)model).getUrl());
			String backUpUrl = ub.getRequestUrlAndr(((ShowOnDemand)model).getUrlBackUp());
			loadingAndParsingTask.execute(url, backUpUrl);
		}
	}
	
}
