package com.sam.gogoeat.view.support

import androidx.fragment.app.Fragment
import com.sam.gogoeat.view.MainActivity

open class BaseFragment : Fragment() {

    protected fun showLoading() {
        (requireActivity() as? MainActivity)?.showLoading()
    }

    protected fun dismissLoading() {
        (requireActivity() as? MainActivity)?.dismissLoading()
    }
}