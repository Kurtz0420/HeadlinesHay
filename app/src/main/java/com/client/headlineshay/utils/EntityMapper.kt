package com.client.headlineshay.utils


/*Interface for interchanging entity(Article Fetched From API)-domainModel(Local Model of article)*/

interface EntityMapper<Entity, DomainModel>{

    fun mapFromArticleNetwork(entity: Entity) : DomainModel

    fun mapToArticleNetwork(domainModel: DomainModel) : Entity

}