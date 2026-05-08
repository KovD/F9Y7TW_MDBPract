import pymongo as mongo
import json

client = mongo.MongoClient("mongodb+srv://kovacsd435:------@bookdb2026.mbjbb7u.mongodb.net/")


db = client["luxusbarDB"] 

#Annyit módosítottam az órai feladathoz képest, hogy én nem hoztam létre új collectionoket / adatbázist hanem amit a már korábban
#feltöltöttem a felhőbe, azokat használom.
bar_coll = db["luxusbar"]
termek_coll = db["termek"]
dolgozo_coll = db["dolgozo"]



print("--- a) Összes luxusbár ---")
for bar in bar_coll.find():
    print(bar)

print("\n--- b) Termék lekérdezése (ID: t1) ---")
t1 = termek_coll.find_one({"_TermID": "t1"})
print(t1)

print("\n--- c) Dolgozók, akik idősebbek 30 évnél ---")
for idos in dolgozo_coll.find({"kor": {"$gt": 30}}):
    print(idos)

print("\n--- d) Bárok száma városonként (Csoportosítás) ---")
pipeline_group = [
    {"$group": {"_id": "$varos", "barokSzama": {"$sum": 1}}}
]
for stat in bar_coll.aggregate(pipeline_group):
    print(f"Város: {stat['_id']} - Bárok száma: {stat['barokSzama']}")

print("\n--- e) Termékek átlagára ---")
pipeline_avg = [
    {"$group": {"_id": None, "atlagAr": {"$avg": "$Ar_db"}}}
]
atlag_eredmeny = list(termek_coll.aggregate(pipeline_avg))
if atlag_eredmeny:
    print(f"Átlagár: {atlag_eredmeny[0]['atlagAr']:.0f} Ft")


print("\n--- Módosítások és Törlések ---")

termek_coll.update_one(
    {"_TermID": "t2"}, 
    {"$set": {"termeknev": "Prémium Kézműves Gin (debug:frissít)"}}
)
print("termék neve frissítve.")

dolgozo_coll.delete_one({"_id": "d3"})
print("kódú dolgozó törölve.")

torolt = dolgozo_coll.delete_many({"kor": {"$lt": 25}})
print(f"25 évnél fiatalabb dolgozók eltávolítva. Törölt darabszám: {torolt.deleted_count}")