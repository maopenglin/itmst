<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
   
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
   <style>
form *{padding:0; margin:0}
form{margin:20px; background:#eee; padding:5px 10px;}
input{margin:0 8px 0 15px; height:20px; line-height:20px;width:200px;}
input[type="submit"]{widows:100px;}
fieldset{padding:10px; border:1px solid #000;}
table{width:100%; border:0; border-collapse:collapse; line-height:30px; margin:10px 0;}
table th{text-align:right; width:40%; font-weight:normal;}
table td span{color:#a40000}
input.rightformcss,select.rightformcss,textarea.rightformcss{border:1px solid green;padding:1px;}
.failmsg{color:#a40000;}
.msgvaluecss{font-style:italic;}
input.failformcss,select.failformcss,textarea.failformcss{border:1px solid #a40000;padding:1px;}
</style>

<script>

$(function(){
	
    $("input[class*=:required]").after("<span> 必填项</span>")
});
 //弹出信息样式设置
Vanadium.config = {
    valid_class: 'rightformcss',//验证正确时表单样式
    invalid_class: 'failformcss',//验证失败时该表单样式
    message_value_class: 'msgvaluecss',//这个样式是弹出信息中调用值的样式
    advice_class: 'failmsg',//验证失败时文字信息的样式
    prefix: ':',
    separator: ';',
    reset_defer_timeout: 100
}
//验证类型及弹出信息设置
Vanadium.Type = function(className, validationFunction, error_message, message, init) {
  this.initialize(className, validationFunction, error_message, message, init);
};
Vanadium.Type.prototype = {
  initialize: function(className, validationFunction, error_message, message, init) {
    this.className = className;
    this.message = message;
    this.error_message = error_message;
    this.validationFunction = validationFunction;
    this.init = init;
  },
  test: function(value) {
    return this.validationFunction.call(this, value);
  },
  validMessage: function() {
    return this.message;
  },
  invalidMessage: function() {
    return this.error_message;
  },
  toString: function() {
    return "className:" + this.className + " message:" + this.message + " error_message:" + this.error_message
  },
  init: function(parameter) {
    if (this.init) {
      this.init(parameter);
    }
  }
};
Vanadium.setupValidatorTypes = function() {
Vanadium.addValidatorType('empty', function(v) {
    return  ((v == null) || (v.length == 0));
  });
//***************************************以下为验证方法,使用时可仅保留用到的判断
Vanadium.addValidatorTypes([
	//匹配大小写的等值
    ['equal', function(v, p) {
      return v == p;
    }, function (_v, p) {
      return '输入的值必须与<span class="' + Vanadium.config.message_value_class + '">' + p + '相符\(区分大小写\)</span>.'
    }],
    //不匹配大小写的等值
    ['equal_ignore_case', function(v, p) {
      return v.toLowerCase() == p.toLowerCase();
    }, function (_v, p) {
      return '输入的值必须与<span class="' + Vanadium.config.message_value_class + '">' + p + '相符\(不区分大小写\)</span>.'
    }],
    //是否为空
    ['required', function(v) {
      return !Vanadium.validators_types['empty'].test(v);
    }, '此项不可为空!'],
    //强制选中 
    ['accept', function(v, _p, e) {
      return e.element.checked;
    }, '必须接受!'],
    //
    ['integer', function(v) {
      if (Vanadium.validators_types['empty'].test(v)) return true;
      var f = parseFloat(v);
      return (!isNaN(f) && f.toString() == v && Math.round(f) == f);
    }, '请输入一个正确的整数值.'],
    //数字
    ['number', function(v) {
      return Vanadium.validators_types['empty'].test(v) || (!isNaN(v) && !/^\s+$/.test(v));
    }, '请输入一个正确的数字.'],
    //
    ['digits', function(v) {
      return Vanadium.validators_types['empty'].test(v) || !/[^\d]/.test(v);
    }, '请输入一个非负整数,含0.'],
    //只能输入英文字母
    ['alpha', function (v) {
      return Vanadium.validators_types['empty'].test(v) || /^[a-zA-Z\u00C0-\u00FF\u0100-\u017E\u0391-\u03D6]+$/.test(v)   //% C0 - FF (? - ?); 100 - 17E (? - ?); 391 - 3D6 (? - ?)
    }, '请输入英文字母.'],
    //仅限ASCII编码模式下输入英文字母
    ['asciialpha', function (v) {
      return Vanadium.validators_types['empty'].test(v) || /^[a-zA-Z]+$/.test(v)   //% C0 - FF (? - ?); 100 - 17E (? - ?); 391 - 3D6 (? - ?)
    }, '请在ASCII下输入英文字母.'],
	//英文字母或正数
    ['alphanum', function(v) {
      return Vanadium.validators_types['empty'].test(v) || !/\W/.test(v)
    }, '请输入英文字母或正数.'],
	//邮箱验证
    ['email', function (v) {
      return (Vanadium.validators_types['empty'].test(v) || /\w{1,}[@][\w\-]{1,}([.]([\w\-]{1,})){1,3}$/.test(v))
    }, '邮箱格式不正确,请检查.正确格式例如mrthink@gmail.com'],
    //网址
    ['url', function (v) {
      return Vanadium.validators_types['empty'].test(v) || /^(http|https|ftp):\/\/(([A-Z0-9][A-Z0-9_-]*)(\.[A-Z0-9][A-Z0-9_-]*)+)(:(\d+))?\/?/i.test(v)
    }, '请输入正确的网址,比如:http://www.mrthink.net'],
    //日期格式
    ['date_au', function(v) {
      if (Vanadium.validators_types['empty'].test(v)) return true;
      var regex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
      if (!regex.test(v)) return false;
      var d = new Date(v.replace(regex, '$2/$1/$3'));
      return ( parseInt(RegExp.$2, 10) == (1 + d.getMonth()) ) && (parseInt(RegExp.$1, 10) == d.getDate()) && (parseInt(RegExp.$3, 10) == d.getFullYear() );
    }, '请输入正确的日期格式,比如:28/05/2010.'],
    //输入固定长度字符串
    ['length',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          return v.length == parseInt(p)
        }
        ;
      },
      function (_v, p) {
        return '输入字符长度等于<span class="' + Vanadium.config.message_value_class + '">' + p + '</span>个.'
      }
    ],
    //
    ['min_length',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          return v.length >= parseInt(p)
        }
        ;
      },
      function (_v, p) {
        return '输入字符长度不低于<span class="' + Vanadium.config.message_value_class + '">' + p + '</span>个.'
      }
    ],
    ['max_length',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          return v.length <= parseInt(p)
        }
        ;
      },
      function (_v, p) {
        return '输入字符长度不大于<span class="' + Vanadium.config.message_value_class + '">' + p + '</span>个.'
      }
    ],
	//判断密码是相同
    ['same_as',
      function (v, p) {
        if (p === undefined) {
          return true
        } else {
          var exemplar = document.getElementById(p);
          if (exemplar)
            return v == exemplar.value;
          else
            return false;
        }
        ;
      },
      function (_v, p) {
        var exemplar = document.getElementById(p);
        if (exemplar)
          return '两次密码输入不相同.';
        else
          return '没有可参考值!'
      },
      "",
      function(validation_instance) {
        var exemplar = document.getElementById(validation_instance.param);
        if (exemplar){
          jQuery(exemplar).bind('validate', function(){
            jQuery(validation_instance.element).trigger('validate');
          });
        }
      }
    ],
	//ajax判断是否存在值
    ['ajax',
      function (v, p, validation_instance, decoration_context, decoration_callback) {
        if (Vanadium.validators_types['empty'].test(v)) return true;
        if (decoration_context && decoration_callback) {
          jQuery.getJSON(p, {value: v, id: validation_instance.element.id}, function(data) {
            decoration_callback.apply(decoration_context, [[data], true]);
          });
        }
        return true;
      }]
    ,
	//正则匹配,此用法不甚理解
    ['format',
      function(v, p) {
        var params = p.match(/^\/(((\\\/)|[^\/])*)\/(((\\\/)|[^\/])*)$/);        
        if (params.length == 7) {
          var pattern = params[1];
          var attributes = params[4];
          try
          {
            var exp = new RegExp(pattern, attributes);
            return exp.test(v);
          }
          catch(err)
          {
            return false
          }
        } else {
          return false
        }
      },
      function (_v, p) {
        var params = p.split('/');
        if (params.length == 3 && params[0] == "") {
          return '输入的值必须与 <span class="' + Vanadium.config.message_value_class + '">' + p.toString() + '</span> 相匹配.';
        } else {
          return '提供的值与<span class="' + Vanadium.config.message_value_class + '">' + p.toString() + '</span>不匹配.';
        }
      }
    ]
  ])
  if (typeof(VanadiumCustomValidationTypes) !== "undefined" && VanadiumCustomValidationTypes) Vanadium.addValidatorTypes(VanadiumCustomValidationTypes);
};
</script>
<title>IOS 应用程序打包</title>
  </head>
  
  <body>
 <div id="demo">
    <form id="iform" name="iform" enctype="multipart/form-data" method="post" action="design">

    <fieldset>
          <legend>IOS 打包 素材提交</legend>
          
          <table>
          
          
    <tr><th><label >userId 用户id :</label></td><td align="left"><input id="checkempty" value="10155" name="site_id" class=":required" /></td></tr>
     <tr><th><label >projectId 项目id :</label></td><td align="left"><input id="checkempty" value="13155" name="project_id" class=":required" /></td></tr>
       
 <tr><th><label >apiUrl 服务器接口地址 :</label></td><td align="left"><input id="checkempty" value="http://api.cmstop.dev/mobile/index.php?" name="api_url" class=":required" /></td></tr>

<tr><th><label for="checkempty">appTopic 协议地址 (区分大小写):</label></td><td align="left"><input id="checkempty" value="appPrefix中文" name="app_profile" class=":required" /></td></tr>
<tr><th><label for="checkempty">appName 应用程序中文名称 :</label></td><td align="left"><input id="checkempty" value="项目名字" name="app_name" class=":required" /></td></tr>
<tr><th><label for="checkempty">xgId 信鸽id :</label></td><td align="left"><input id="checkempty" value="2323232323233" name="app_xgid" class=":required" /></td></tr>
<tr><th><label for="checkempty">xgKey 信鸽 key :</label></td><td align="left"><input id="checkempty" value="xgAppKey" name="app_xgkey" class=":required" /></td></tr>
<tr><th><label for="checkempty">appVersion 程序版本 :</label></td><td align="left"><input id="checkempty" value="appversion" name="app_version" class=":required" /></td></tr>
 
 <tr><th><label for="checkempty">chyang id :</label></td><td align="left"><input id="checkempty" value="app_sh_key" name="app_sh_key" class=":required" /></td></tr>
<tr><th><label for="checkempty">chyang sec :</label></td><td align="left"><input id="checkempty" value="app_sh_sercet" name="app_sh_sercet" class=":required" /></td></tr>
 
 
<tr><th><label for="checkempty">mtaKey qq统计key :</label></td><td align="left"><input id="checkempty" value="mtakey" name="mta_key" class=":required" /></td></tr>
<tr><th><label for="checkempty">identifier 应用程序标示 :</label></td><td align="left"><input id="checkempty" value="identifier" name="app_identifier" class=":required" /></td></tr>
<tr><th><label for="checkempty">urlSchemes 应用程序协议分享协议地址（多个用,号分割） :</label></td><td align="left"><input id="checkempty" value="app_urlschemes" name="app_urlschemes" class=":required" /></td></tr>

<tr><th><label for="checkempty">icon 应用图标(size:57*57) :</label></td><td align="left"><input type="file" id="checkempty" name="icon"  /></td></tr>
<tr><th><label for="checkempty">icon114 应用图标(size:114*114) :</label></td><td align="left"><input type="file" id="checkempty" name="icon114"  /></td></tr>
<tr><th><label for="checkempty">icon29 应用图标(size:29*29) :</label></td><td align="left"><input type="file" id="checkempty" name="icon29"  /></td></tr>
<tr><th><label for="checkempty">icon58 应用图标(size:58*58) :</label></td><td align="left"><input type="file" id="checkempty" name="icon58"  /></td></tr>
<tr><th><label for="checkempty">icon80 应用图标(size:80*80) :</label></td><td align="left"><input type="file" id="checkempty" name="icon80" /></td></tr>
<tr><th><label for="checkempty">icon120 应用图标(size:120*120) :</label></td><td align="left"><input type="file" id="checkempty" name="icon120"  /></td></tr>
<tr><th><label for="checkempty">Default 应启动动画(size:320*480) :</label></td><td align="left"><input type="file" id="checkempty" name="Default" /></td></tr>
<tr><th><label for="checkempty">Default@2x 应启动动画(size:640*960) :</label></td><td align="left"><input type="file" id="checkempty" name="Default@2x"  /></td></tr>
<tr><th><label for="checkempty">Default-568h@2x 应启动动画(size:640*1136) :</label></td><td align="left"><input type="file" id="checkempty" name="Default-568h@2x"  /></td></tr>


<tr><th></th><td><input type="submit" value="提交表单" style="width:80px; padding:0.2em; height:30px;" /></td></tr>
</table>
    </form>
    </div>   

  </body>
</html>
