package blkbk.fuzzy_waddle

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import blkbk.fuzzy_waddle.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle clicks on menu items
        val headerView = findViewById<TextView>(R.id.selectedMenuItem)
        when (item.itemId) {
            R.id.action_settings -> {
                headerView.text = "Настройки"
                return true
            }

            R.id.save_settings -> {
                headerView.text = "Сохранить"
                return true
            }

            R.id.open_settings -> {
                headerView.text = "Открыть"
                return true
            }

            R.id.marathon -> {
                MaterialAlertDialogBuilder(this).setTitle("О Marathon Skills 2023").setMessage(
                        "Marathon Skills - фестиваль бега, проводимый каждый год в разных частях мира. Может быть три зачета: Полный Марафон, Полумарафон и  забег для новичков - таким образом фестиваль подходит всем.\n" +
                        "\n" +
                        "В прошлых годах марафон был проведен в Осаке, Япония (2014); Лейпциг, Германия (2013); Ханой, Вьетнам (2012) и Йорк, Англия (2011).\n" +
                        "\n" +
                        "В этом году, Marathon Skills очень зрелищно, продет в  Сан-Паоло, Бразилия, он должен быть самым большим фестивалем бега. Это финансовый центр Бразилии и самый большой город в Южной Америке.Сан-Пауло увидят тысячи бегунов, которые будут бежать мимо небоскребов, зеленые парки и великолепная архитектура.\n" +
                        "\n" +
                        "Этот фестиваль привлек рекордное количество бегунов со всего мира.  особое внимание будет обращено на участников из Кении и Ямайки, поскольку мы надеемся увидеть победителя 2014 года. (Эфиоп закончил гонку за 45 минут 4 секунды.)\n" +
                        "\n" +
                        "Атмосфера праздника обещает развлечения для всех зрителей.\n" +
                        "\n" +
                        "События:\n" +
                        "\n" +
                        "- Программа \"Самба\" Полный Марафон начнется в Руа-Ду-Американо в 6 утра.\n" +
                        "\n" +
                        "- Программа \"Джонго\" Полумарафон начнет в 7 утра Бегуны будут стартовать от недалеко от пересечения улицы Руа Ciniciata и Авенида.\n" +
                        "\n" +
                        "- Программа \"Капоэйра\" в 5 км забег для новичков начнется в 15 часов Наши новички побегут от Мемориала Унинове.\n" +
                        "\n" +
                        "Спасибо всем волонтерам, которые будут помогать нам проводить марафон!").show()
                return true
            }

            R.id.log -> {
                val arr = calculate(window.decorView.rootView)
                if (arr[0] == R.id.maleRadio) Log.d("LOGGING", String.format("Male, %s", arr.contentToString()))
                if (arr[0] == R.id.femaleRadio) Log.d("LOGGING", String.format("Female, %s", arr.contentToString()))
                Log.d("LOGGING", arr.contentToString())
                return true
            }
            // add more cases as needed for other menu items
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun calculate(view: View): Array<Any> {
        val weight = findViewById<TextInputEditText>(R.id.weight)
        val height = findViewById<TextInputEditText>(R.id.height)
        val age = findViewById<TextInputEditText>(R.id.age)
        val gender = findViewById<RadioGroup>(R.id.radioGroup)
        val checked_gender = gender.checkedRadioButtonId
        var bmr = 0.0

        if (weight.text.toString() == "" || height.text.toString() == "" || age.text.toString() == "" || checked_gender.equals(
                null
            )
        ) return arrayOf("Fill the values")

        try {
            if (checked_gender == R.id.maleRadio) bmr =
                660.0 + (13.7 * weight.text.toString().toDouble()) +
                        (5 * height.text.toString().toDouble()) - (6.8 * age.text.toString()
                    .toDouble())
            else if (checked_gender == R.id.femaleRadio) bmr =
                655.0 + (9.6 * weight.text.toString().toDouble()) +
                        (1.8 * height.text.toString().toDouble()) - (4.7 * age.text.toString()
                    .toDouble())
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR: Incorrect format", Toast.LENGTH_LONG).show()
            return arrayOf("Incorrect")
        }

        val activities = mapOf<Double, String>(
            round(bmr) to "Normal",
            round(bmr * 1.2) to "Passive lifestyle",
            round(bmr * 1.375) to "Low activity",
            round(bmr * 1.55) to "Medium activity",
            round(bmr * 1.725) to "High activity",
            round(bmr * 1.9) to "Extreme activity"
        )

        val activitiesKeys = activities.keys.toDoubleArray()

        MaterialAlertDialogBuilder(this)
            .setTitle("Your calories measurement")
            .setMessage(
                String.format(
                    "${activitiesKeys[0]}: Normal value (w/o activity ratio)\n" +
                            "${activitiesKeys[1]}: ${activities[round(bmr * 1.2)]}\n" +
                            "${activitiesKeys[2]}: ${activities[round(bmr * 1.375)]}\n" +
                            "${activitiesKeys[3]}: ${activities[round(bmr * 1.55)]}\n" +
                            "${activitiesKeys[4]}: ${activities[round(bmr * 1.725)]}\n" +
                            "${activitiesKeys[5]}: ${activities[round(bmr * 1.9)]}"
                )
            ).setPositiveButton("OK") { dialog, which -> }.show()

        return arrayOf(checked_gender, bmr)
    }

    fun activitiesInfo(view: View) {
        MaterialAlertDialogBuilder(this).setTitle("Activity Info").setMessage(
            "Passive: This level of activity is characterized by a sedentary lifestyle with little or no physical activity. Examples of this level of activity include sitting at a desk all day, watching TV, or playing video games.\n\n" +
                    "Low: This level of activity is characterized by light physical activity, such as walking or doing household chores. Examples of this level of activity include taking a leisurely walk, gardening, or cooking.\n\n" +
                    "Medium: This level of activity is characterized by moderate physical activity, such as jogging, cycling, or swimming. Examples of this level of activity include going for a run, playing a sport, or doing a workout at the gym.\n\n" +
                    "High: This level of activity is characterized by intense physical activity, such as competitive sports or heavy lifting. Examples of this level of activity include playing a contact sport, weightlifting, or doing a high-intensity workout.\n\n" +
                    "Extreme: This level of activity is characterized by extreme physical exertion, such as marathon running or mountaineering. Examples of this level of activity include running a marathon, climbing a mountain, or doing an extreme endurance event."
        ).setPositiveButton("OK") { dialog, which -> }.show()
    }
}