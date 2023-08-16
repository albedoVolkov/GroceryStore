package com.example.grocerystore.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.data.helpers.DishUIState
import com.example.grocerystore.data.source.local.utilsData.ListTypeConverter

@Database(entities = [DishUIState::class, CategoryUIState::class], version = 1, exportSchema = true)
@TypeConverters(ListTypeConverter::class)
abstract class GroceryStoreDatabase : RoomDatabase() {

    abstract fun dishesDao(): DishesDao
	abstract fun categoriesDao(): CategoriesDao

    	companion object {
			@Volatile
			private var INSTANCE: GroceryStoreDatabase? = null

			fun getDataBase(context: Context): GroceryStoreDatabase {
				val tempInstance = INSTANCE
				if (tempInstance != null) {
					return tempInstance
				}
				synchronized(this) {
					val instance = Room.databaseBuilder(
						context.applicationContext,
						GroceryStoreDatabase::class.java,
						"application_DB"
					).build()
					INSTANCE = instance
					return  instance
				}
			}
		}
}
