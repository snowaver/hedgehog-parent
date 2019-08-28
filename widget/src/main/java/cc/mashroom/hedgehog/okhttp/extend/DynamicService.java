package cc.mashroom.hedgehog.okhttp.extend;

import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.http.GET;
import  retrofit2.http.Streaming;
import  retrofit2.http.Url;

public  interface  DynamicService
{
	@Streaming
	@GET
	public  Call<ResponseBody>  download( @Url  String  url );
}
