-printconfiguration config.txt
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-keep class com.race604.flyrefresh.**{*;}

-dontwarn javax.annotation.**
