# K2SS 会計情報管理システム

[K2SS](https://k2ss.info/)の会計管理システムです。  
※ただし、汎用的に利用できることを想定しています。

# セットアップ

以下がインストールされていること

- Scala
- SBT

以下コマンドを実行してください。

```
$ sbt playUpdateSecret && sbt run
```

`ensime`を利用する場合は、事前に以下コマンドの実行が必要です。

```
$ sbt ensimeConfig
```
