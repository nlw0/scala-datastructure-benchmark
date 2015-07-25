scala-datastructure-benchmark
=============================

A simple benchmark to estimate the speed difference of using mutable and immutable Maps as counters.

## Methodology
Microbenchmarks are controversial. But we want to see their results anyway, right?

We have two tests here in this project, one using [Caliper](https://github.com/google/caliper), and another wihout and such tools, using some code [adapted from another test](https://github.com/nlw0/scala-json-benchmark) I did to compare JSON parsing libraries.

We tested three different counter update methods. We used a mutable map, an immutable map generated from a foldleft straight from the input data, and a `var` containing an immutable map.

## Not Caliper

At each test run we generated different sets of random strings. Each string had 6 characters from 'a' to 'e'. That means we generated at most 5<sup>6</sup> = 15,625 different elements. We tested the methods one after the other, 11 times for each different input data size of 10k, 100k, 1M and 10M strings. In the first two cases the minimum count found at the maps was 1, while in the other the values were close to 35 and 540.

The minimum, median and maximum times from the 11 runs from each method and input sizes can be found in the tables below. We can notice the mutable map did exhibit the best times in all cases, about 40% faster than the other methods. The times from the foldLeft and the var-of-immutable methods were pretty much the same. One final interesting observation is that we can notice a somewhat linear increase in the times from 10k, 100k and 1M samples, but for 10M the time was slower. We don't know what could cause this non-linearity, we tried to use iterators for everything, and the memory consumption from the last two cases should be the same. It could be because of how memory is pre-allocated in immutable maps.

Results follow:

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

## Tests using Caliper

Caliper showed similar results, at most ~40% faster for mutable.
<table>
<tr><th>input size</th><th>mutable</th><th>fold</th><th>var</th></tr>
<tr><td>   8k</td><td>    5.58</td><td>    6.34</td><td>    6.18</td></tr>
<tr><td>  40k</td><td>   30.31</td><td>   34.60</td><td>   36.29</td></tr>
<tr><td> 200k</td><td>  141.35</td><td>  189.70</td><td>  186.97</td></tr>
<tr><td>   1M</td><td>  675.88</td><td> 1004.84</td><td> 1100.15</td></tr> 
<tr><td>   5M</td><td> 5347.58</td><td> 7383.24</td><td> 7608.36</td></tr> 
</table>

![cool graphic](http://i.imgur.com/Ydn6qt7.png)
