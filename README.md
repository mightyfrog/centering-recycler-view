# Centering Recycler View (Android)
A view aligning (center, top, bottom, start, end) RecyclerView.

###centering demo

<img src="website/demo.gif" alt="demo" width="200px" />

Download [ ![Download](https://api.bintray.com/packages/mightyfrog/maven/centering-recycler-view/images/download.svg) ](https://bintray.com/mightyfrog/maven/centering-recycler-view/_latestVersion)
--------

```groovy
compile 'org.mightyfrog.centeringrecyclerview:centeringrecyclerview:1.0.2'
```

Usage
--------
####center(int position)
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.center(100); // 100 = adapter position
```
####top(int position) - vertical orientation
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.top(100); // 100 = adapter position
```
####bottom(int position) - vertical orientation
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.bottom(100); // 100 = adapter position
```
####start(int) - horizontal orientation = left
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.start(100); // 100 = adapter position
```
####end(int) - horizontal orientation = right
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.end(100); // 100 = adapter position
```
####setSelection(int position, int alignment)
```java
CenteringRecyclerView mRecyclerView;
...
mRecyclerView.setSelection(100, CenteringRecyclerView.ALIGN_CENTER); // 100 = adapter position
```

License
--------
Copyright 2015 Shigehiro Soejima.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
