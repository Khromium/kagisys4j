# kagisys4j
鍵をrfid読み取りで開閉する装置

## 仕様
raspberry piに以下をGPIOでつなぎ、rfid読み取りによってドア鍵のツマミを回してくれるような仕組みを構築した。


## 使用したもの
- サーボモータ S03TXF 2BB
- RFIDリーダ RC522
- led(適当なやつ１つ)
- タクトスイッチ(開閉用と登録ボタン用で２つ。長押し判定があるので一つにまとめることもできる)
- 抵抗（LEDとタクトスイッチを使用するのに適当なやつ)

## 動作方法
```
./gradlew jar
```

で単体のjarファイルが出来上がるので、それで実行できます。  
2017年10月25日の時点で最新のraspberry piのカーネル+raspberry pi2ではpi4jのGPIOが動作しなかったので以下のコマンドでカーネルのバージョンを4.4に下げる必要があります。

```
sudo rpi-update 52241088c1da59a359110d39c1875cda56496764
```

## 使用ライブラリ  
- java8
- [pi4j](http://pi4j.com/)
   - [LGPL](http://pi4j.com/license.html)
- [sqlite-jdbc](https://bitbucket.org/xerial/sqlite-jdbc)
   - [Apache License](http://www.apache.org/licenses/LICENSE-2.0)
