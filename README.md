# SuperDownloader

## Introduce

An android application for downloading resource.

Maybe many people find that even if you have an 100MB bandwidth of optical fiber, it's so slowly to download resource most time. In fact, one reason is that the resource server doesn't have enough bandwidth to deal so many requests. But another reason is that your downloader limit your speed to force users paying for VIP.

So I'm extremely hating it. But as a developer, what should I do is to change, not to complain. I decide to make a downloader with three kind of download types - **Url**, **Torrent**, **Magnet Link**.

Android provide a API called **DownloadManager**, so I nearly didn't spend time finishing the first type. Then I started to learn BitTorrent protocol. After having some knowledge about it, I found that it was impossible to realize the second type and the third. So I deleted last two types.

[About P2P](http://baike.baidu.com/link?url=G4rOu-5vlbkAe3cGXaHCLklTU7kRLZImRwLvzR9SfXnQtLLQvHvByGEdl7MYKoamCAB9LIpqFQYmCuH6VP3Y4UyKyUMTlc08BkgbRtVOyC3)

[About BitTorrent](http://baike.baidu.com/item/%E5%AF%B9%E7%AD%89%E7%BD%91%E7%BB%9C/5482934?fromtitle=p2p&fromid=139810)

## Screenshot

### **New**

***Here I inset a browser page, so you can browse meanwhile download.***

![](https://github.com/13608089849/SuperDownloader/blob/master/image/new.jpeg)

### **Current**

***The list of current downloading tasks.***

![](https://github.com/13608089849/SuperDownloader/blob/master/image/current.jpeg)

### **History**

***The list of historical downloaded tasks.***

![](https://github.com/13608089849/SuperDownloader/blob/master/image/history.jpeg)