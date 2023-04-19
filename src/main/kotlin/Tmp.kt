import file.FileUtil
import java.util.regex.Pattern

class Tmp {
}

// 航班时刻,航班号,航班往返地点,乘机人
// $1 8月31日成都T1-西港 $3 $4$5  $2 李辉航
fun main() {
    genResultSet()

    val resultSetCleaned = mutableListOf<String>()

    // 尊敬的旅客，您预定的机票，已经出票成功。乘机人：CHENG/YINGKUENBENNY 行程 ：2018-08-27 深圳-西港 海南航空 HU703 0945起飞1150到达 退改签规定：不可以退改签。 备注：经济舱，签证自理，可托运行李额：20KG温馨提示：请乘客凭护照，提前180分钟到机场办理值机手续，请了解，祝您一路平安！注意：（信息仅供参考，未收到行程单表示没有出票，一个小时内未给请向我司索取） ，谢谢配合！
    val regex =
        Regex("\\s?尊敬的旅客，您预定的机票，已经出票成功。乘机人[:|：](.+)行程 ：(.+) (.+)[瑞丽航空|海南航空](.+) (.+到达).*")
    // 尊敬的旅客，您预定的机票，已经出票成功。乘机人：倪明【JC航空】2018年8月29日杭州T2-西哈努克城、航班号QD701—00：20起飞，03：30抵达
    val regex2 = Regex("\\s?尊敬的旅客，您预定的机票，已经出票成功。乘机人[:|：](.+)【JC航空】(.+)、航班号(.+)—(.+抵达).*")
    // 尊敬的旅客，您预定的机票，已经出票成功。乘机人：朱丽华【JC航空】2018年8月28日 西哈努克城—杭州航班号QD700。18:05起飞，23:15抵达，票号：待定；行李托运额：20kg；退改签条款：不可以退改签。【备注】经济舱、无免费餐食、签证自理。温馨提示：请乘客凭护照，提前180分钟到机场办理值机手续，请了解
    val regex3 = Regex("\\s?尊敬的旅客，您预定的机票，已经出票成功。乘机人[:|：](.+)【JC航空】(.+)航班号(.+)(.+抵达).*")
    // 尊敬的旅客，您8月31日成都T1-西港的机票已经预定成功登机人: 李辉航班号：ZA494 航班时刻：0520（北京时间）-07:50（当地时间）退改签条款：不可退票，可托运行李额：15KG，温馨提示：请乘客1:30之前到达机场凭护照办理登记手续，祝您旅途愉快！谢谢
    val regex4 = Regex("\\s?尊敬的旅客，您(.+)的机票已经预定成功登机人[:|：](.+)航班号：(.+)航班时刻：(.+)-(.+）).*")
    // 尊敬的旅客，您机票已预定成功。乘机人： 凌兵/杨寿兵/王伦波/明聪/雷金兵行程 ：2018-6-1   西哈努克港国际机场-武汉天河国际机场  ZA797  1730-2200不退不改签，可托运行李额：15KG，温馨提示：请乘客提前3小时到达机场凭护照办理登机手续，祝您旅途愉快！
    val regex5 = Regex("\\s?尊敬的旅客，您机票已预定成功。乘机人：(.+)行程 ：(.+)  (.+)  (\\d+-\\d+).*")
    // 尊敬的旅客，您预定的机票，已经出票成功乘机人:焦民，程飞，李晨露（天空吴哥航空）航班号ZA474  6月3日无锡硕放国际机场1号航站楼--西哈努克城，14:25起飞，18:10抵达，行李托运额：15kg。退改签条款：不可以退改签。
    val regex6 =
        Regex("\\s?尊敬的旅客，您预定的机票，已经出票成功乘机人[:|：](.+)（天空吴哥航空）航班号(.+)  (.+)，(.+抵达).*")
    // 尊敬的旅客，您预定的机票，已经出票成功。乘机人:  陈亮，李海峰，陈鹏（天空吴哥航空）航班号ZA493  5月21日西哈努克国际机场—成都双流国际机场T1航站楼22:40起飞，02:10抵达，行李托运额：15kg；退改签条款：不可以退改签。
    val regex7 = Regex("\\s?尊敬的旅客，您预定的机票，已经出票成功。乘机人[:|：](.+)（天空吴哥航空）航班号(.+)  (.+抵达).*")
    // 尊敬的旅客，您预定的机票已经出票成功。乘机人:向定平（天空吴哥航空）航班号ZA494    5月15日双流国际机场T1航站楼—西哈努克国际机场05:20起飞，07:50抵达，行李托运额：15kg；
    val regex8 = Regex("\\s?尊敬的旅客，您预定的机票已经出票成功。乘机人[:|：](.+)（天空吴哥航空）航班号(.+)  (.+抵达).*")

    val resultSetNew = FileUtil.readFile4Line(
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "resultSet.txt"
    ).filter {
        val pattern = Pattern.compile(regex.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex, "$2\t$4\t$3 $5\t$1")
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex2.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex2, "$2\t$3\t$4\t$1")
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex3.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex3, "$2\t$3\t$4\t$1")
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex4.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex4, "$1\t$3\t$4-$5\t$2") // $1 8月31日成都T1-西港 $3 $4$5  $2 李辉航
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex5.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex5, "$2\t$3\t$4\t$1") // $2 $3 $4   $1
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex6.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            val replace = it.replace(regex6, "$3\t$2\t$4\t$1") // $3 $2 $4 $1
            resultSetCleaned.add(replace + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex7.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            // 尊敬的旅客，您预定的机票，已经出票成功。乘机人:  陈亮，李海峰，陈鹏（天空吴哥航空）航班号ZA493  5月21日西哈努克国际机场—成都双流国际机场T1航站楼22:40起飞，02:10抵达，行李托运额：15kg；退改签条款：不可以退改签。
            val replace = it.replace(regex7, "$3")
            val s = replace.split("\t")[0]
            val s1 = s.substring(0, s.length - 15)
            val s2 = s.substring(s.length - 15)
            resultSetCleaned.add("$s1\t$s2" + it.replace(regex7, "\t$2\t$1") + " ---> $it")
            false
        } else true
    }.filter {
        val pattern = Pattern.compile(regex8.pattern)
        val matcher = pattern.matcher(it)
        if (matcher.find()) {
            // 尊敬的旅客，您预定的机票已经出票成功。乘机人:向定平（天空吴哥航空）航班号ZA494    5月15日双流国际机场T1航站楼—西哈努克国际机场05:20起飞，07:50抵达，行李托运额：15kg；
            val replace = it.replace(regex8, "$3")
            val s = replace.split("\t")[0]
            val s1 = s.substring(0, s.length - 15)
            val s2 = s.substring(s.length - 15)
            resultSetCleaned.add("$s1\t$s2" + it.replace(regex8, "\t$2\t$1") + " ---> $it")
            false
        } else true
    }

    FileUtil.append2File(
        resultSetCleaned.joinToString("\n") {
            it.replace(Regex(" --->.+"), "")
                .replace("\\s", "")
        },
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "resultSetCleaned.txt"
    )

    FileUtil.newFile(
        resultSetNew.joinToString("\n"),
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "resultSet.txt"
    )
}

private fun genResultSet() {
    val regex = Regex("(成功)|(行李)");
    val listA = FileUtil.readFile4Line(
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "a.txt"
    ).run {
        this.filter { line -> line.contains(regex) }
            .filter { !it.contains("支付了") && !it.contains("支付成功了") }
            .map { it.replace(" ", " ") }
            .toTypedArray()
    }
    val listB = FileUtil.readFile4Line(
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "b.txt"
    ).run {
        this.filter { line -> line.contains(regex) }
            .filter { !it.contains("支付了") && !it.contains("支付成功了") }
            .map { it.replace(" ", " ") }
            .toTypedArray()
    }

    val mutableListOf = listA.toMutableList();
    mutableListOf.addAll(listB)

    FileUtil.newFile(
        mutableListOf.joinToString("\n"),
        "/Users/holland/repo/holland/utils/src/main/kotlin",
        "resultSet.txt"
    )
}