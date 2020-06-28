This is a Java API which wraps the REST-based [Flickr API](http://www.flickr.com/services/api/).

It is a new library built on top of the FlickrJAPI from http://flickrj.sourceforge.net/.

It resolves the following issues that currently exist in FlickrJ:  
1. Non-Java5 Syntax. This caused a lot of WARNINGs, and didn't leverage the latest JDK features.  
2. Support for Android and GAE. The old FlickrJ uses SOAP API which is not supported on GAE.  
3. New Flickr OAuth 1a support: http://www.flickr.com/services/api/auth.oauth.html  
4. Completely re-written with JSON response format.  
5. Now built by Maven instead of Ant.  
6. Added some fuctions missings in the original FlickrJ, such as GalleryInterface and CollectionInterface.  
7. Use https by default for all API requests(thanks to Paul Bourke): http://code.flickr.net/2014/04/30/flickr-api-going-ssl-only-on-june-27th-2014/. Please use version 2.1.0+

In case of login with Android WebView, following configuration is required.
```
webView.getSettings().setJavaScriptEnabled(true);
webView.getSettings().setDomStorageEnabled(true);
```

This project is now available on both [Google Code](http://code.google.com/p/flickrj-android/) and [GitHub](https://github.com/yuyang226/FlickrjApi4Android).

-Toby Yu(yuyang226@gmail.com)
