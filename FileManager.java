package com.baidu.ueditor.hunter;

import java.util.List;
import java.util.Map;

import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.zmn.mng.upload.cloud.storage.QiniuStorage;

public class FileManager {

	private String dir = null;
	private int count = 0;
	
	public FileManager ( Map<String, Object> conf ) {
		this.dir = (String)conf.get( "dir" );
		this.count = (Integer)conf.get( "count" );
	}
	
	public State listFile ( int index ) {
		MultiState state = null;
		
		QiniuStorage qiniuUtils = new QiniuStorage();
		List<String> list = qiniuUtils.findList(this.dir);
		
		if (list.size() == 0 || index < 0 || index > list.size() ) {
			state  = new MultiState( true );
		} else {
			state = (MultiState) this.getState( list.subList(index, index +  this.count > list.size() ? list.size(): index +  this.count) );
		}
		
		state.putInfo( "start", index );
		state.putInfo( "total", list.size() );
		
		return state;
		
	}
	
	private State getState ( List<String> urls ) {
		
		MultiState state = new MultiState( true );
		BaseState fileState = null;
		
		for ( String url : urls ) {
			fileState = new BaseState( true );
			fileState.putInfo( "url", url);
			state.addState( fileState );
		}
		
		return state;
	}
	
}
