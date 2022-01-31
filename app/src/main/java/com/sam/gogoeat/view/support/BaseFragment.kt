package com.sam.gogoeat.view.support

import androidx.fragment.app.Fragment
import com.sam.gogoeat.utils.Util.launchWhenStarted
import com.sam.gogoeat.view.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

open class BaseFragment : Fragment() {

    protected fun showLoading() {
        (requireActivity() as? MainActivity)?.showLoading()
    }

    protected fun dismissLoading() {
        (requireActivity() as? MainActivity)?.dismissLoading()
    }

    protected fun <T> Flow<T>.collectDataState(doSomething:((t: T)-> Unit)) {
        this.onEach {
            doSomething.invoke(it)
        }.launchWhenStarted(viewLifecycleOwner)
    }
}