# RangSliderWithNumber
A digital selector with bubbles and ruler

## Preview ##
![preview](https://github.com/freecats/Resources/blob/master/NumberSelect.gif)
## How to get it work？ ##

without bubbles and ruler

```java
<com.example.freecats.numberselect.view.RangeSliderWithNumber
        android:id="@+id/rsn_number"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="10dp"
        android:layout_marginStart="10dp"
        android:layout_marginTop="16dp"
        app:rsn_insideRangeLineColor="#0bd97f"
        app:rsn_insideRangeLineStrokeWidth="5dp"
        app:rsn_min="0"
        app:rsn_numberTextColor="#0bd97f"
        app:rsn_numberTextSize="15sp"
        app:rsn_outsideRangeLineColor="#f0f0f0"
        app:rsn_outsideRangeLineStrokeWidth="5dp" />
```

with bubbles and ruler

```java
<com.example.freecats.numberselect.view.RangeSliderWithNumber
        android:id="@+id/rsn_bubble"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="10dp"
        android:layout_marginStart="10dp"
        android:layout_marginTop="16dp"
        app:rsn_insideRangeLineColor="#0bd97f"
        app:rsn_insideRangeLineStrokeWidth="5dp"
        app:rsn_isShowBubble="true"
        app:rsn_isShowRuler="true"
        app:rsn_max="200"
        app:rsn_min="50"
        app:rsn_numberTextColor="#ffffff"
        app:rsn_numberTextSize="15sp"
        app:rsn_outsideRangeLineColor="#f0f0f0"
        app:rsn_outsideRangeLineStrokeWidth="5dp" />
```


single number selector

```java
     <com.example.freecats.numberselect.view.SeekBarWithNumber
        android:id="@+id/sbn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="30dp"
        android:layout_marginEnd="22dp"
        android:layout_marginStart="22dp"
        android:layout_marginTop="30dp"
        app:sbn_insideRangeLineColor="#0bd97f"
        app:sbn_insideRangeLineStrokeWidth="5dp"
        app:sbn_isShowBubble="true"
        app:sbn_isShowRuler="true"
        app:sbn_min="10"
        app:sbn_rulerInterval="twenty"
        app:sbn_numberTextColor="#ffffff"
        app:sbn_numberTextSize="15sp"
        app:sbn_outsideRangeLineColor="#f0f0f0"
        app:sbn_outsideRangeLineStrokeWidth="5dp"
        app:sbn_rulerColor="@color/color_gray_66" />
```
All Done!

Attributes support as below

Attributes                      | format 
------------                    | -------------
 rsn_min                        | integer 
 rsn_max                        | reference 
 rsn_targetColor                | color  
 rsn_numberTextColor            | color 
 rsn_numberTextSize             | dimension 
 rsn_numberMarginBottom         | dimension 
 rsn_isShowBubble               | boolean  
 rsn_bubbleResource             | reference 
 rsn_isLineRound                | boolean 
 rsn_circleFocusBitmap          | reference 
 rsn_insideRangeLineColor       | color 
 rsn_outsideRangeLineColor      | color 
 rsn_insideRangeLineStrokeWidth | dimension 
 rsn_outsideRangeLineStrokeWidth| dimension 
 rsn_isShowRuler                | boolean 
 rsn_rulerTextColor             | color 
 rsn_rulerColor                 | color 
 rsn_rulerMarginTop             | dimension 
 rsn_rulerAndTextMargin         | dimension 
 rsn_rulerTextSize              | dimension 
 rsn_rulerInterval              | integer 


# License



    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
