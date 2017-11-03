var SPE = "am23m4WEG5m346m7weE";
var ACCOUNT_INTO_IN_COOKIE = "ACCOUNT_INTO_IN_COOKIE";
var RECORD_ACCOUNT_CHECKBOX = "RECORD_ACCOUNT_CHECKBOX";
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}



$(function() {
	$('.loginbox').css({
		'position' : 'absolute',
		'left' : ($(window).width() - 692) / 2
	});
	$(window).resize(function() {
		$('.loginbox').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 692) / 2
		});
	});
	$('.qrcode').css({
		
	});
	readAccountInCookie();
	initErrorSpan();
});





var $1 = function(id){
	return document.getElementById(id);
}



function beforeLogin(){
	var flag = true;
	//处理账号cookie
	recordAccountInCookie();
	
	//验证码校验
	// flag = checkImg();
	return flag;
}
//记录账号信息入cookie
function recordAccountInCookie(){
	var account = $.cookie(ACCOUNT_INTO_IN_COOKIE);
	var ifRecord = $.cookie(RECORD_ACCOUNT_CHECKBOX);
	if(account != null && 
			account != undefined && account != '' ){
		 $.cookie(ACCOUNT_INTO_IN_COOKIE,null);　
	}
	if(ifRecord != null && 
			ifRecord != undefined && ifRecord != '' ){
		$.cookie(RECORD_ACCOUNT_CHECKBOX,null);　
	}
	var checkbox = $("#checkbox").is(':checked');
	if(checkbox && checkbox == true){
		 $.cookie(RECORD_ACCOUNT_CHECKBOX, "true", {expires: 7, secure:false});
		 $.cookie(ACCOUNT_INTO_IN_COOKIE, $(".loginuser").val()+SPE+$(".loginpwd").val(), { expires: 7, secure:false});
	}
}

//读取记录账号信息从cookie
function readAccountInCookie(){
	var account = $.cookie(ACCOUNT_INTO_IN_COOKIE);
	var ifRecord = $.cookie(RECORD_ACCOUNT_CHECKBOX);
	if(account != null && 
			account != undefined && account != '' 
				&& account.split(SPE).length == 2){
		
    	var accountStr = account.split(SPE);
	    if(accountStr != null && accountStr.length == 2){
	    	$(".loginuser").val(accountStr[0] == null ? "" : accountStr[0]);
	    	$(".loginpwd").val(accountStr[1] == null ? "" : accountStr[1]);
	    		
		}else{
			$(".loginuser").val("");
	    	$(".loginpwd").val("");
		}
	}  	
	if(ifRecord == 'true'){
		$("#checkbox").attr("checked",true);
	}else{
		$("#checkbox").attr("checked",false);
	}
	
}

