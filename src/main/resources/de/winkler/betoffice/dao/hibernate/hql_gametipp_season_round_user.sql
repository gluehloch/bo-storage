select
    gametipp from GameTipp gametipp join fetch
    gametipp.user user join fetch
    gametipp.game game join
    gametipp.game.gameList gameList
where
    gametipp.game.gameList.id = :roundId and
    gametipp.game.id = game.id and
    gametipp.user.id = :userId
