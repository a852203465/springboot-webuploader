# webuploader

#### 介绍
基于webuploader的分片上传(后台springbooot)

<a name="需要知识点"></a>
### 需要知识点
- 基于spring boot开发的。
- WebUploader，WebUploader是由Baidu WebFE(FEX)团队开发的一个简单的以HTML5为主，FLASH为辅的现代文件上传组件。
- redis,key-value存储系统，在这里我把redis用作存储文件路径来使用。

<a name="启动项目"></a>
### 启动项目

1. main方法直接运行：  
(1)找到App启动类（com.unionman.webuploader包下）  
(2)执行main方法。  
(3)然后用浏览器访问：<http://localhost:8080>

<a name="功能分析"></a>
## 功能分析

<a name="分块上传"></a>
### 分块上传
&emsp;&emsp;分块上传可以说是我们整个项目的基础，像断点续传、暂停这些都是需要用到分块。
分块这块相对来说比较简单。前端是采用了webuploader，分块等基础功能已经封装起来，使用方便。
借助webUpload提供给我们的文件API,前端就显得异常简单。

<a name="秒传功能"></a>
### 秒传功能

&emsp;&emsp;秒传功能，相信大家都体现过了，网盘上传的时候，发现上传的文件秒传了。其实原理稍微有研究过的同学应该知道，其实就是检验文件MD5，记录下上传到系统的文件的MD5,在一个文件上传前先获取文件内容MD5值或者部分取值MD5，然后在匹配系统上的数据。  
&emsp;&emsp;*Breakpoint-http*实现秒传原理，客户端选择文件之后，点击上传的时候触发获取文件MD5值，获取MD5后调用系统一个接口（/index/checkFileMd5），查询该MD5是否已经存在（我在该项目中用redis来存储数据，用文件MD5值来作key，value是文件存储的地址。）接口返回检查状态，然后再进行下一步的操作。相信大家看代码就能明白了。  
&emsp;&emsp;嗯，前端的MD5取值也是用了webuploader自带的功能，这还是个不错的工具。

<a name="断点续传"></a>
### 断点续传
&emsp;&emsp;断点续传，就是在文件上传的过程中发生了中断，人为因素（暂停）或者不可抗力（断网或者网络差）导致了文件上传到一半失败了。然后在环境恢复的时候，重新上传该文件，而不至于是从新开始上传的。  
&emsp;&emsp;前面也已经讲过，断点续传的功能是基于分块上传来实现的，把一个大文件分成很多个小块，服务端能够把每个上传成功的分块都落地下来，客户端在上传文件开始时调用接口快速验证，条件选择跳过某个分块。  
&emsp;&emsp;实现原理，就是在每个文件上传前，就获取到文件MD5取值，在上传文件前调用接口（/index/checkFileMd5，没错也是秒传的检验接口）如果获取的文件状态是未完成，则返回所有的还没上传的分块的编号，然后前端进行条件筛算出哪些没上传的分块，然后进行上传。  

##参考文献

[1]http://fex.baidu.com/webuploader/
[2]http://www.zuidaima.com/blog/2819949848316928.htm  
[3]https://my.oschina.net/feichexia/blog/212318







































