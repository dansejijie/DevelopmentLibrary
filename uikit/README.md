v 1.0.0
    云信 3.5.5
    环信 3.3.0 依赖引入

    uikit 只有 XX xx= NimClient.getInstance().getService(XX.class); 注释掉，其他保持不变
    环信sdk引入在com.netease.nimlib包里


    5月9日：
        1、extra里的MsgViewHolderThumbBase里注释了 进度显示文本
        2、MsgViewHolderBase里注释了显示已阅读的代码
        3、修复在第一次加载失败时，失败view一直显示的问题，BaseFechLoadAdapter加了getFetchMoreView函数，在MessageListPanelEx里的refreshMessageListEx函数里使用