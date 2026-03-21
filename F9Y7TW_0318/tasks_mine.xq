xquery version "3.1";

(:
doc('/db/F9Y7TW_XML1.xml')//luxusbar
:)

(:
for $b in doc('/db/F9Y7TW_XML1.xml')//luxusbar[varos="Budapest"]
return
    <bar_nev>{data($b/nev)}</bar_nev>:)

(:
for $p in doc('/db/F9Y7TW_XML1.xml')//patron[kor > 40]
return
<idosebb_patron>
    <nev>{data($p/nev/vezeteknev)} {data($p/nev/keresztnev)}</nev>
</idosebb_patron>:)

(:
for $t in doc('/db/F9Y7TW_XML1.xml')//termek[allergen="Nincs"]
return
<mentes_termek>
    <nev>{data($t/termeknev)}</nev>
</mentes_termek>

:)

(:
for $t in doc('/db/F9Y7TW_XML1.xml')//termek
return
<termek_ar>
    <nev>{data($t/termeknev)}</nev>
    <ar>{data($t/Ar_db)}</ar>
</termek_ar>:)

(:
for $t in doc('/db/F9Y7TW_XML1.xml')//termek
let $szallito := doc('/db/F9Y7TW_XML1.xml')//besszallitoceg[@SzallID = $t/@SzallID]
return
<beszallitas>
    <termek>{data($t/termeknev)}</termek>
    <ceg>{data($szallito/nev)}</ceg>
</beszallitas>:)

(:
for $t in doc('/db/F9Y7TW_XML1.xml')//termek[termeknev="Prémium Vodka"]
return update value $t/Ar_db with $t/Ar_db + 1000:)

(:
count(doc('/db/F9Y7TW_XML1.xml')//dolgozo):)

(:
max(doc('/db/F9Y7TW_XML1.xml')//termek/Ar_db):)

(:
avg(doc('/db/F9Y7TW_XML1.xml')//patron/kor):)