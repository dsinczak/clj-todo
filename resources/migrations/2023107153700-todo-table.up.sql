CREATE TABLE IF NOT EXISTS TODO (
                                    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    title    TEXT NOT NULL ,
                                    content  TEXT NOT NULL ,
                                    done     BOOLEAN NOT NULL DEFAULT FALSE,
                                    priority INTEGER NOT NULL DEFAULT 0,
                                    created  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated  TIMESTAMP WITH TIME ZONE);