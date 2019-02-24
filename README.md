# Flickrの写真を表示するアプリ

## ビルド環境

Android Studio 3.3.1を使用しています。

## ビルド方法

1. FlickrでDevelopper登録を行い、APIキーを取得してください。  
https://www.flickr.com/services/apps/create/

2. 取得したAPIキーはapp/src/main/res/values/api_keys.xmlに以下のフォーマットに従って保存してください。
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="flickr_api_key">「取得したAPIキー」</string>
</resources>
```

3. Android StudioでRun、またはDebugを実行