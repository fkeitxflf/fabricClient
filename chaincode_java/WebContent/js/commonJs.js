    function myformatter(date) {
    	var y = date.getFullYear();
    	var m = date.getMonth() + 1;
    	var d = date.getDate();
    	return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
    	+ (d < 10 ? ('0' + d) : d);
    	}
    
    function myparser(s) {
    	if (!s)
    	return new Date();
    	var ss = (s.split('-'));
    	var y = parseInt(ss[0], 10);
    	var m = parseInt(ss[1], 10);
    	var d = parseInt(ss[2], 10);
    	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    	return new Date(y, m - 1, d);
    	} else {
    	return new Date();
    	}
    	}
    
	$(function() {
	    $.extend($.fn.validatebox.defaults.rules,{
	    	phoneRex: {
	    		validator:function(value) {
	    		var rex=/^1[3-8]+\d{9}$/;
	    		var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
	    	    if(rex.test(value)||rex2.test(value))
	    	    {
	    	      return true;
	    	    }else
	    	    {
	    	       return false;
	    	    }
	    	    },
	    	    message: '请输入正确电话或手机格式'
	    	  }
	    	});
	});
    
