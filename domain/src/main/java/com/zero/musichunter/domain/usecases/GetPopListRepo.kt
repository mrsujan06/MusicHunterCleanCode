package com.zero.musichunter.domain.usecases

import com.zero.musichunter.domain.exception.NoConnectedException
import com.zero.musichunter.domain.functions.StatementSingle
import com.zero.musichunter.domain.model.Repo
import com.zero.musichunter.domain.repository.RepoRepository
import com.zero.musichunter.domain.usecases.base.Logger
import com.zero.musichunter.domain.usecases.base.SingleUseCase
import com.zero.musichunter.domain.usecases.base.UseCaseScheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * This class is an implementation of [SingleUseCase] that represents a use case for
 * retrieving a collection of all [Repo].
 */
class GetPopListRepo @Inject constructor(
    private val repoRepository: RepoRepository,
    useCaseScheduler: UseCaseScheduler? = null, logger: Logger? = null
) : SingleUseCase<List<Repo>>(useCaseScheduler, logger) {
    override fun build(): Single<List<Repo>> {

        val getCachePopListRepo = repoRepository.getCachePopListRepo()

        val cacheSingle = getCachePopListRepo.map {
            if (it.isEmpty()) throw NoConnectedException else it
        }
        val netSingle = repoRepository.getPopListFromNet()
            .flatMap {
                repoRepository.savePopListRepo(it).andThen(getCachePopListRepo)
            }

        return StatementSingle.ifThen(repoRepository.isConnected, netSingle, cacheSingle)
    }


}