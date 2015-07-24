scala-datastructure-benchmark
=============================

A simple benchmark to estimate the speed difference of using mutable and immutable Maps as counters.

## Methodology
This is not an extremely careful benchmark, but it employs some techniques to try to make it as fair as possible for a _simple_ benchmark. Basically we run the different methods one after the other a number of times and look at the statistics of the running times. A lot of the code here was [adapted from another test](https://github.com/nlw0/scala-json-benchmark) to compare JSON parsing libraries.

We tested three different counter update methods. We used a mutable map, an immutable map generated from a foldleft straight from the input data, and a `var` containing an immutable map.

At each test run we generated different sets of random strings. Each string had 6 characters from 'a' to 'e'. That means we generated at most 5<sup>6</sup> = 15,625 different elements. We tested the methods one after the other, 11 times for each different input data size of 10k, 100k, 1M and 10M strings. In the first two cases the minimum count found at the maps was 1, while in the other the values were close to 35 and 540.

The minimum, median and maximum times from the 11 runs from each method and input sizes can be found in the tables below. We can notice the mutable map did exhibit the best times in all cases, about 40% faster than the other methods. The times from the foldLeft and the var-of-immutable methods were pretty much the same. One final interesting observation is that we can notice a somewhat linear increase in the times from 10k, 100k and 1M samples, but for 10M the time was slower. We don't know what could cause this non-linearity, we tried to use iterators for everything, and the memory consumption from the last two cases should be the same.

## Results

### 10k samples
<table>
<tr><th>method</th><th>min</th><th>med</th><th>max</th></tr>
<tr><td>mutable       </td><td>    6</td><td>    7</td><td>  130</td></tr>
<tr><td>immutable-fold</td><td>    9</td><td>   12</td><td>   36</td></tr>
<tr><td>immutable-var </td><td>    9</td><td>   10</td><td>   77</td></tr>
</table>

### 100k samples
<table>
<tr><th>method</th><th>min</th><th>med</th><th>max</th></tr>
<tr><td>mutable       </td><td>   60</td><td>   63</td><td>   68</td></tr>
<tr><td>immutable-fold</td><td>   95</td><td>   98</td><td>  111</td></tr>
<tr><td>immutable-var </td><td>   96</td><td>   98</td><td>  113</td></tr>
</table>

### 1M
<table>
<tr><th>method</th><th>min</th><th>med</th><th>max</th></tr>
<tr><td>mutable       </td><td>  586</td><td>  601</td><td>  666</td></tr>
<tr><td>immutable-fold</td><td>  989</td><td>  992</td><td> 1,109</td></tr>
<tr><td>immutable-var </td><td>  990</td><td>  995</td><td> 1,093</td></tr>
</table>

### 10M
<table>
<tr><th>method</th><th>min</th><th>med</th><th>max</th></tr>
<tr><td>mutable       </td><td> 7,350</td><td> 8,537</td><td>11,279</td></tr>
<tr><td>immutable-fold</td><td>11,162</td><td>13,899</td><td>15,893</td></tr>
<tr><td>immutable-var </td><td>11,897</td><td>14,374</td><td>17,421</td></tr>
</table>
