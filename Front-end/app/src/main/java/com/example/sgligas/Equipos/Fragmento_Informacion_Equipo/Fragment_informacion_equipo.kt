import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sgligas.Equipos.Fragmento_Informacion_Equipo.ItemAdapter
import com.example.sgligas.Equipos.Fragmento_Informacion_Equipo.item
import com.example.sgligas.R



class Fragment_informacion_equipo : Fragment() {
    private val itemList = mutableListOf<item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_informacion_equipo, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_informacion_equipo)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemAdapter = ItemAdapter(itemList)
        recyclerView.adapter = itemAdapter

        val presidente = arguments?.getString("presidente") ?: "No disponible"
        val fechaFundacion = arguments?.getString("fechaFundacion") ?: "No disponible"
        val colores = arguments?.getString("colores") ?: "No disponible"

        // Limpia la lista itemList antes de agregar nuevos datos
        itemList.clear()
        itemList.add(item("PRESIDENTE", presidente))
        itemList.add(item("FECHA DE FUNDACIÓN", fechaFundacion))
        itemList.add(item("COLORES", colores))

        itemAdapter.notifyDataSetChanged()

        return view
    }
}




