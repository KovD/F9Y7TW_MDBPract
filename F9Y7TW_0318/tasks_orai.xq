xquery version "3.1";

(: Task1 :doc('/db//F9Y7TW_XML.xml')//vendeg :)


(: for $s in doc('/db/F9Y7TW_XML.xml')//szakacs[vegzettseg="Szakközépiskola"]
return
<szakacs>  
    <SzakacsID>{data($s/@szkod)}</SzakacsID>  
    <nev>{data($s/nev)}</nev>
    <reszleg>{data($s/reszleg)}</reszleg>
    <vegzettseg>{data($s/vegzettseg)}</vegzettseg>
</szakacs>
:)

(:
for $e in doc("/db/F9Y7TW_XML.xml")//etterem[csillag = 5]
return
    <etterem>
        <EtteremID>{data($e/@id)}</EtteremID>
        <Nev>{data($e/nev)}</Nev>  
        <Cim>{$e/cim}</Cim>
        <Csillag>{data($e/csillag)}</Csillag>
    </etterem>
    :)


(: Task4 :for $g in doc("/db/F9Y7TW_XML.xml")//gyakornok[muszak = "Délután"]
return
<gyakornok>
    <GyakornokID>{data($g/@gykod)}</GyakornokID>
    <Nev>{data($g/nev)}</Nev>
    <Kezdet>{data($g/gyakorlat/kezdete)}</Kezdet>
    <Muszak>{data($g/muszak)}</Muszak>
</gyakornok>
:)

(: Task5 for $v in doc("/db/F9Y7TW_XML.xml")//vendeg
let $rendelesek :=  doc("/db/F9Y7TW_XML.xml")//rendeles[@vkod = $v/@vkod]
for $r in $rendelesek
return
<join_rend>
    <nev>{data($v/nev)}</nev>
    <osszeg>{data($r/osszeg)}</osszeg>
</join_rend>
:)

(: Task6 for $l in doc("/db/F9Y7TW_XML.xml")//rendeles
return
update value $l/osszeg with $l/osszeg + 1000
:)



(: Task7/1 count(doc("/db/F9Y7TW_XML.xml")//rendeles) :)
(: Task7/2 sum(doc("/db/F9Y7TW_XML.xml")//rendeles/osszeg) :)
(: Task7/3 avg(doc("/db/F9Y7TW_XML.xml")//rendeles/osszeg) :)
