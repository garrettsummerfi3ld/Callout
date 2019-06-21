package io.github.garrettsummerfi3ld.callout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.zip.Inflater

class ContactLoaderFragment : Fragement() {
    companion object {
        fun newInstance(): ContactLoaderFragment {
            return ContactLoaderFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.ContactLoaderFragment, container, false)
    }
}
