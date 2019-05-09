# nimbus-javaversion-filecontrol-maven-plugin

## 機能概要
コンパイルするJavaバージョン毎の差異を吸収するための、コメント化（文字列置換）、ファイル名変換コピーを行うプラグインです。

## インストール方法

Mavenのpom.xmlに以下の記載を追加する。

```xml
        <plugins>
            ・・・
            <plugin>
                <groupId>com.github.nimbus-org</groupId>
                <artifactId>nimbus-javaversion-filecontrol-maven-plugin</artifactId>
                <version>X.X</version>
                <configuration>
                    ・・・
                </configuration>
            </plugin>
            ・・・
        </plugins>
```

## 使用方法（replace）

### configuration

| 設定キー | 説明 | 必須 | デフォルト値 | 設定例 |
|---|---|---|---|---|
| javaVersion | コンパイルに使用するJavaのバージョン ||System.getProperty("java.specification.version")で取得できるJavaバージョン| 8 |
| checkJavaVersions | 置換チェックするJavaのバージョン | | 6,7,8,9 | 6,7,8,9 （※１） |
| fromDir | 置換元ファイルルートディレクトリ | ○ | 無し | ${basedir}/src/main/java |
| toDir | 置換後ファイルコピー先ルートディレクトリ | ○ | 無し | ${basedir}/target/gen-src |
| replaceTargetDirs | 置換元ファイルディレクトリ | ○ | 無し | jp/ossc/nimbus/\*\* (※１)|
| fromFileExtention | 置換対象ファイル拡張子 | ○ | 無し | javapp |
| toFileExtention | 置換後ファイル拡張子 | | java | java |
| encoding | ファイルの読み込み書き込みEncoding | | システムのデフォルトエンコーディング | UTF-8 |

※１ 複数指定が可能なため、`<param>XXXX</param>`で複数記載する。

### 設定例

+ Java8でコンパイル
+ Java6,7,8,9をチェック
+ /src/main/java 配下のソースが置換対象
+ /target/gen-src に置換後ファイルを配置
+ jp/ossc/nimbus/\*\* にある（パッケージである）ソースが置換対象
+ javappが置換対象ファイル拡張子
+ 置換後のファイル拡張子はjava
+ ソースファイルのエンコーディングはUTF-8

```xml
        <plugins>
            ・・・
            <plugin>
                <groupId>com.github.nimbus-org</groupId>
                <artifactId>commentout-by-javaversion-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>replace</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>replace</goal>
                        </goals>
                        <configuration>
                            <javaVersion>8</javaVersion>
                            <fromDir>${basedir}/src/main/java</fromDir>
                            <toDir>${basedir}/target/gen-src</toDir>
                            <fromFileExtention>javapp</fromFileExtention>
                            <toFileExtention>java</toFileExtention>
                            <encoding>UTF-8</encoding>
                            <checkJavaVersions>
                                <param>6</param>
                                <param>7</param>
                                <param>8</param>
                                <param>9</param>
                            </checkJavaVersions>
                            <replaceTargetDirs>
                                <param>jp/ossc/nimbus/**</param>
                            </replaceTargetDirs>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            </plugin>
            ・・・
        </plugins>
```
### コメント置換ルール
ルールに従った文字列を記載することで、コメントアウトのON/OFFを行う

#### コメントブロック記載ルール
| 説明 | 設定文字列ルール | 設定例 |
|---|---|---|
| コメント開始 | "@START" + **"条件"** + "JAVA" + **"バージョン"** + "@" | @START=JAVA9@|
| コメント終了 | "@END" + **"条件"** + "JAVA" + **"バージョン"** + "@" | @END=JAVA9@|

#### 使用可能条件
| 条件文字列 | 説明 | 設定例 | 設定例説明 |
|---|---|---|---|
| = | 対象のJavaバージョンと等しい場合に有効となる |@START=JAVA9@ ～～～ @END=JAVA9@| Java9でコンパイルする場合にのみ有効となる |
| > | 対象のJavaバージョンより大きい場合に有効となる |@START>JAVA7@ ～～～ @END>JAVA7@| Java7より大きいバージョンでコンパイルする場合にのみ有効となる |
| >= | 対象のJavaバージョン以上の場合に有効となる |@START>=JAVA8@ ～～～ @END>=JAVA8@|  Java8以上のバージョンでコンパイルする場合にのみ有効となる  |
| < | 対象のJavaバージョンより小さい場合に有効となる |@START\<JAVA7@ ～～～ @END\<JAVA7@| Java7より小さいバージョンでコンパイルする場合にのみ有効となる |
| <= | 対象のJavaバージョン以下の場合に有効となる |@START<=JAVA7@ ～～～ @END<=JAVA7@| Java7以下のバージョンでコンパイルする場合にのみ有効となる |

### ソース記載例

Javaバージョンによるimportの制御を行う例

```java
@START<JAVA9@
import sun.reflect.ReflectionFactory;
@END<JAVA9@
@START=JAVA9@
import jdk.internal.reflect.ReflectionFactory;
@END=JAVA9@
```

上記の設定でJava9でコンパイルした場合

```java
/* **Java Version Difference Comment Start**
import sun.reflect.ReflectionFactory;
**Java Version Difference Comment End** */

import jdk.internal.reflect.ReflectionFactory;
```

上記の設定でJava8,7,6でコンパイルした場合

```java
import sun.reflect.ReflectionFactory;

/* **Java Version Difference Comment Start**
import jdk.internal.reflect.ReflectionFactory;
**Java Version Difference Comment End** */
```


## 使用方法（copy）

### configuration

| 設定キー | 説明 | 必須 | デフォルト値 | 設定例 |
|---|---|---|---|---|
| javaVersion | コンパイルに使用するJavaのバージョン ||System.getProperty("java.specification.version")で取得できるJavaバージョン| 8 |
| targetJavaVersionAndFiles| Javaのバージョンと置換チェックするファイル | ○ | 無し | 6,jp/ossc/nimbus/**/.*.jpp（※１） |
| fromDir | 置換元ファイルルートディレクトリ | ○ | 無し | ${basedir}/src/main/java |
| toDir | 置換後ファイルコピー先ルートディレクトリ | ○ | 無し | ${basedir}/target/gen-src |
| toFileExtention | 置換後ファイル拡張子 | | java | java |

※１ 複数指定が可能なため、`<param>XXXX</param>`で複数記載する。

### 設定例

+ Java8でコンパイル
+ Java6でコンパイルする場合は正規表現（jp/ossc/nimbus/service/\*\*/.\*.jppとjp/ossc/nimbus/util/\*\*/.\*.jpp）をコピー対象とする
+ Java7でコンパイルする場合は正規表現（jp/ossc/nimbus/service/\*\*/.\*.jppとjp/ossc/nimbus/util/\*\*/.\*.jpp）をコピー対象とする
+ Java8でコンパイルする場合は正規表現（jp/ossc/nimbus/service/\*\*/.\*.jppとjp/ossc/nimbus/util/\*\*/.\*.jpp）をコピー対象とする
+ /src/main/java 配下のソースが置換対象
+ /target/gen-src に置換後ファイルを配置
+ 置換後のファイル拡張子はjava

```xml
        <plugins>
            ・・・
            <plugin>
                <groupId>com.github.nimbus-org</groupId>
                <artifactId>commentout-by-javaversion-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>copy</goal>
                        </goals>
                        <configuration>
                            <javaVersion>8</javaVersion>
                            <fromDir>${basedir}/src/main/java</fromDir>
                            <toDir>${basedir}/target/gen-src</toDir>
                            <targetJavaVersionAndFiles>
								<param>6,jp/ossc/nimbus/service/**/.*.jpp,jp/ossc/nimbus/util/**/.*.jpp</param>
								<param>7,jp/ossc/nimbus/service/**/.*.jpp,jp/ossc/nimbus/util/**/.*.jpp</param>
								<param>8,jp/ossc/nimbus/service/**/.*.jpp,jp/ossc/nimbus/util/**/.*.jpp</param>
                            </targetJavaVersionAndFiles>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            ・・・
        </plugins>
```
