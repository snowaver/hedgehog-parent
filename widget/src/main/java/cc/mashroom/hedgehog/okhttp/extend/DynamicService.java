package cc.mashroom.hedgehog.okhttp.extend;

import  okhttp3.ResponseBody;
import  retrofit2.Call;
import  retrofit2.http.GET;
import  retrofit2.http.Url;

public  interface  DynamicService
{
	@GET
	public  Call<ResponseBody>  get(@Url String url);
}
