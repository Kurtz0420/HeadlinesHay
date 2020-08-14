package com.client.headlineshay.utils


/*Interface for interchanging entity(Article Fetched From API)-domainModel(Local Model of article)*/

interface EntityMapper<Entity, DomainModel>{

  fun mapToArticleLocal(entity: Entity) : DomainModel

  fun mapFromArticleLocal(domainModel: DomainModel) : Entity

}