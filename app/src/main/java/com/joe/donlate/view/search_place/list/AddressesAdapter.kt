package com.joe.donlate.view.search_place.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.joe.donlate.data.Address
import com.joe.donlate.databinding.ListAddressItemBinding
import com.joe.donlate.view.base.BaseHolder
import com.joe.donlate.view.base.MutableListAdapter
import java.util.*

class AddressesAdapter(private val addressClick: MutableLiveData<String>) : MutableListAdapter<Address, BaseHolder<Address>>() {
    override val items: LinkedList<Address> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Address> {
        return AddressesHolder(
            ListAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            addressClick
        )
    }
}

class AddressesHolder(private val binding: ListAddressItemBinding, private val addressClick: MutableLiveData<String>) :
    BaseHolder<Address>(binding) {
    override fun bind(data: Address) {
        binding.jibunAddress.text = data.jibunAddress
        binding.roadAddress.text = data.roadAddress
        itemView.setOnClickListener {
            addressClick.value = data.jibunAddress
        }
    }
}