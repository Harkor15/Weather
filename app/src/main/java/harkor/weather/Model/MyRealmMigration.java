package harkor.weather.Model;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if(oldVersion==0){
            RealmObjectSchema objectSchema=schema.get("DatabaseObject");
            objectSchema.addField("cords",String.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("cords",obj.getDouble("longitude")+"x"+
                                    obj.getDouble("latitude"));
                            Log.d("wpd",obj.getString("cords"));
                        }
                    });
            oldVersion++;
        }
        if(oldVersion==1){
            RealmObjectSchema objectSchema=schema.get("DatabaseObject");
            if (objectSchema != null) {
                objectSchema.removeField("cords");
                oldVersion++;
                Log.d("wpd",oldVersion+" Cords deleted");
            }

        }
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MyRealmMigration);
    }
}
